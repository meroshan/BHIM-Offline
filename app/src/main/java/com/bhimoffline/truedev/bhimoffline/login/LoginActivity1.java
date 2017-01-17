package com.bhimoffline.truedev.bhimoffline.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.bhimoffline.truedev.bhimoffline.R;
import com.bhimoffline.truedev.bhimoffline.activity.MainActivity;

/**
 * Created by rahul on 1/4/2017.
 */

public class LoginActivity1 extends AppCompatActivity {

    public static final String myPref = "truedev.bhimoffline.app";
    public SharedPreferences.Editor editor;
    TextInputEditText login_phone_no;
    AutoCompleteTextView login_bank_name;
    Button login_continue, playstore_bhim, playstore_phonepe;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        login_phone_no = (TextInputEditText) findViewById(R.id.login_mobile_no);
        login_bank_name = (AutoCompleteTextView) findViewById(R.id.login_bank_name);
        login_continue = (Button) findViewById(R.id.login_continue);
        playstore_bhim = (Button) findViewById(R.id.playstore_bhim);
        playstore_phonepe = (Button) findViewById(R.id.playstore_phonepe);

        String bankNames[] = {"Abhyudaya Co-op Bank", "Allahabad Bank", "Andhra Bank", "Apna Sahakari Bank", "Axis Bank", "Bank of Baroda",
                "Bank of India", "Bank of Maharashtra", "Bhartiya Mahila Bank", "Canara Bank", "Central Bank of India", "Corporation Bank",
                "DCB Bank", "Dena Bank", "Federal Bank", "Gujrat State Co-op Bank", "Hasti Co-op Bank", "HDFC Bank", "ICICI Bank", "IDBI Bank",
                "Indian Bank", "Indian Overseas Bank", "IndusInd Bank", "Janata Sahakari Bank", "Karnataka Bank Ltd", "Karur Vysya Bank", "Kotak Mahindra Bank",
                "Mehsan Urban Co-op Bank", "Nainital Bank", "NKGSB Co-op Bank", "Oriental Bank of Commerce", "Punjab & Maharastra Co-op Bank", "Punjab National Bank",
                "Punjab & Sind Bank", "RBL Bank", "Saraswat Bank", "South Indian Bank", "State Bank of Bikaner & Jaipur", "State Bank of Hyderabad",
                "State Bank of India", "State Bank of Mysore", "State Bank of Patiala", "State Bank of Travancore", "Syndicate Bank", "Tamilnad Mercantile Bank",
                "UCO Bank", "Union Bank of India", "United Bank of India", "Vijaya Bank", "Yes Bank"};

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, bankNames);
        login_bank_name.setAdapter(adapter);

        login_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login_phone_no.getText().toString().length() == 10 && (login_bank_name.getText().toString().length() > 0)) {
                    if (saveToSharedPreference(login_phone_no.getText().toString(), login_bank_name.getText().toString())) {
                        startActivity(new Intent(MainActivity.getInstance(), MainActivity.class));
                    }
                }
            }
        });

        playstore_bhim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bhimApp = new Intent(Intent.ACTION_VIEW);
                bhimApp.setData(Uri.parse("https://play.google.com/store/apps/details?id=in.org.npci.upiapp"));
                startActivity(bhimApp);
            }
        });

        playstore_phonepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phonepe = new Intent(Intent.ACTION_VIEW);
                phonepe.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.phonepe.app"));
                startActivity(phonepe);
            }
        });
    }

    public Boolean saveToSharedPreference(String phone_no, String bank_name) {
        sharedPreferences = getSharedPreferences(myPref, 0);
        editor = sharedPreferences.edit();
        editor.putString("phone_no", phone_no);
        editor.putString("bank_name", bank_name);
        editor.putBoolean("isLoggedIn", true);
        return editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "onBackPressed", Toast.LENGTH_SHORT).show();
        //MainActivity.getInstance().finish();
        moveTaskToBack(true);
    }
}














