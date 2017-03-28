package com.bhimoffline.truedev.bhimoffline.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhimoffline.truedev.bhimoffline.R;
import com.bhimoffline.truedev.bhimoffline.activity.MainActivity;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.bhimoffline.truedev.bhimoffline.login.LoginActivity.myPref;

/**
 * Created by rahul on 26-Mar-17.
 */

public class Fragment1 extends android.support.v4.app.Fragment implements View.OnClickListener {
    public final static String LOGGED = "isLoggedIn";
    public final static String PHONE_NO = "phone_no";
    public final static String BANK_NAME = "bank_name";
    public final static String BALANCE = "balance";
    public final static String MINI_BALANCE = "mini_balance";
    public final static String LAST_UPDATED = "last_updated";
    public final static String MINI_LAST_UPDATED = "mini_last_updated";
    TextView balance_card_balance, mini_card_balance;
    TextView balance_card_last_updated, mini_card_last_updated;
    Button update_balance, update_mini;
    SharedPreferences sharedPreferences;

    ImageView share_whatsApp;
    ImageView rate_play_store;
    int flag = 0;
    private FirebaseAnalytics mFirebaseAnalytics;
    private BottomSheetLayout bottomSheet;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Fragment1.MessageEvent message) {

        Log.d("zxcvb1", message.message);

        if (flag == 1) {
            Log.d("zxcvb2", "flag1");
            parseBalance(message.message);
        } else if (flag == 2) {
            Log.d("zxcvb2", "flag2");
            parseMiniStatement(message.message);
        }
        String balance = message.message;

    }

    private void parseMiniStatement(String message) {
        sharedPreferences = this.getActivity().getSharedPreferences(myPref, 0);
        if (message.toLowerCase().equals("transactions")) {
            mini_card_balance.setTextSize(44);
        } else {
            if (message.length() > 1) {
                message = message.substring(1, message.length() - 1);
            }
            mini_card_balance.setTextSize(24);
            mini_card_balance.setText(message);
            sharedPreferences.edit().putString(MINI_BALANCE, message).apply();
            String date = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(new Date());
            sharedPreferences.edit().putString(MINI_LAST_UPDATED, date).apply();
            mini_card_last_updated.setText("last updated : " + date);
        }
    }

    private void parseBalance(String message) {
        String balance = parsebankBalance(message);
        String date = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(new Date());

        sharedPreferences = this.getActivity().getSharedPreferences(myPref, 0);
        if (balance.equals("null")) {
            balance = sharedPreferences.getString(BALANCE, "Balance");
            date = sharedPreferences.getString(LAST_UPDATED, "never");
        } else {
            MainActivity.updateBalance(balance);
            change_balance_text_size();
            sharedPreferences.edit().putString(BALANCE, balance).apply();
            sharedPreferences.edit().putString("last_updated", date).apply();
        }

        if (!balance.equals("Balance"))
            balance = "₹" + " " + balance;
        balance_card_balance.setText(balance);
        balance_card_last_updated.setText(date);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(myPref, 0);
        balance_card_balance = (TextView) view.findViewById(R.id.balance_card_balance);
        balance_card_last_updated = (TextView) view.findViewById(R.id.balance_card_last_updated);
        mini_card_balance = (TextView) view.findViewById(R.id.mini_card_balance);
        mini_card_last_updated = (TextView) view.findViewById(R.id.mini_card_last_updated);

        String balance = sharedPreferences.getString(BALANCE, "Balance");
        //sharedPreferences.edit().clear().apply();

        String mini_balance = sharedPreferences.getString(MINI_BALANCE, "Transactions");

        if (!balance.toLowerCase().equals("balance")) {
            balance = "₹" + " " + balance;
            change_balance_text_size();
        }
        balance_card_balance.setText(balance);
        balance_card_last_updated.setText(sharedPreferences.getString(LAST_UPDATED, "never"));

        if (mini_balance.toLowerCase().equals("transactions")) {
            mini_card_balance.setTextSize(48);
        } else {
            mini_card_balance.setTextSize(24);
        }
        mini_card_balance.setText(mini_balance);
        mini_card_last_updated.setText(sharedPreferences.getString(MINI_LAST_UPDATED, "never"));

        bottomSheet = (BottomSheetLayout) view.findViewById(R.id.bottomsheet);
        bottomSheet.setPeekOnDismiss(true);

        balance_card_balance.setOnClickListener(this);
        update_balance = (Button) view.findViewById(R.id.update_balance);
        update_balance.setOnClickListener(this);
        update_mini = (Button) view.findViewById(R.id.update_mini);
        update_mini.setOnClickListener(this);

        share_whatsApp = (ImageView) view.findViewById(R.id.share_whatsApp);
        rate_play_store = (ImageView) view.findViewById(R.id.rate_play_store);
        balance_card_balance = (TextView) view.findViewById(R.id.balance_card_balance);
        balance_card_last_updated = (TextView) view.findViewById(R.id.balance_card_last_updated);
        //get_started_install = (Button) findViewById(R.id.get_started_install);

        share_whatsApp.setOnClickListener(this);
        rate_play_store.setOnClickListener(this);
        //get_started_install.setOnClickListener(this);

        return view;
    }


    private void change_balance_text_size() {
        balance_card_balance.setTextSize(56);
        //balance_card_balance.setTextColor(Color.parseColor("#323232"));
    }

    private void shareOnWhatsApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String message = "Don't have Internet connection and still want to check your bank balance and send money? Download BHIM Offline" +
                " app and experience offline banking on a click.\n https://play.google.com/store/apps/details?id=com.sohaari.bhimoffline";
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(shareIntent);
    }

    private Boolean askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                showMessage();
                return false;
            }
        }
        return true;
    }


    private void showMessage() {
        android.support.v4.app.DialogFragment df = new MyDialog();
        df.show(getFragmentManager(), "tag");
    }

    public void makeCall(final String ussdCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent fetchBalanceIntent = new Intent(Intent.ACTION_CALL);
                fetchBalanceIntent.setData(Uri.parse("tel:" + ussdCode + Uri.encode("#")));

                //To Do - for the first time after getting permission, fetchBalanceIntent is not starting. Fix this.
                startActivity(fetchBalanceIntent);

            }
        }).start();
    }

    private String parsebankBalance(String message) {
        String tokens[] = message.toLowerCase().split("\\s");
        ArrayList<String> keywords = new ArrayList<>(Arrays.asList(tokens));


        Log.d("zxcvb", message);
        int pos;
        String balance = "null";
        if (keywords.contains("rs.") || keywords.contains("rs")) {
            if (keywords.contains("rs.")) {
                pos = keywords.indexOf("rs.");
                if (pos + 1 < keywords.size()) {
                    balance = keywords.get(pos + 1);
                    balance = balance.substring(0, balance.length() - 1);
                }
            } else if (keywords.contains("rs")) {
                pos = keywords.indexOf("rs");
                if (pos + 1 < keywords.size()) {
                    balance = keywords.get(pos + 1);
                    balance = balance.substring(0, balance.length() - 1);
                }
            }
        }
        return balance;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_whatsApp:
                shareOnWhatsApp();
                break;
            case R.id.rate_play_store:
                Intent bhimOffline = new Intent(Intent.ACTION_VIEW);
                bhimOffline.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sohaari.bhimoffline"));
                startActivity(bhimOffline);
                break;
            case R.id.update_balance:
                flag = 1;
                if (askForPermission()) {
                    makeCall("*99*3");
                }
                break;
            case R.id.update_mini:
                flag = 2;
                if (askForPermission()) {
                    //Toast.makeText(getActivity(), "update_mini", Toast.LENGTH_SHORT).show();
                    makeCall("*99*6");
                }
                break;
//            case R.id.get_started_install:
//                Toast.makeText(instance, "Opening Play Store", Toast.LENGTH_SHORT).show();
//                Intent phonePe = new Intent(Intent.ACTION_VIEW);
//                phonePe.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.phonepe.app"));
//                startActivity(phonePe);
//                break;
        }
    }

    public static class MessageEvent {
        public final String message;

        //@android.support.test.espresso.core.deps.guava.eventbus.Subscribe
        public MessageEvent(String message) {
            this.message = message;
        }
    }
}
