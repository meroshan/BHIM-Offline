package com.bhimoffline.truedev.bhimoffline.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bhimoffline.truedev.bhimoffline.R;
import com.bhimoffline.truedev.bhimoffline.fragment.Fragment1;
import com.bhimoffline.truedev.bhimoffline.fragment.Fragment2;
import com.bhimoffline.truedev.bhimoffline.service.BackgroundService;
import com.bhimoffline.truedev.bhimoffline.service.USSDAccessibilityService;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import static com.bhimoffline.truedev.bhimoffline.fragment.Fragment1.BALANCE;
import static com.bhimoffline.truedev.bhimoffline.fragment.Fragment1.PHONE_NO;
import static com.bhimoffline.truedev.bhimoffline.login.LoginActivity.myPref;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_ITEM = "arg_selected_item";
    public static String PACKAGE_NAME;
    public static String UNABLE_TO_FETCH = "Unable to fetch balance";
    public static TextView user_detail_card_balance;
    SharedPreferences sharedPreferences;
    TextView user_detail_card_bank_name;
    TextView user_detail_card_upi_address;
    ImageView bank_logo;
    String TAG = "mainactivity";
    private int mSelectedItem;

    public static void updateBalance(String balance) {
        if (user_detail_card_balance != null) {
            if (balance.toLowerCase().equals("balance")) {
            } else {
                balance = "₹" + " " + balance;
            }
            user_detail_card_balance.setText(balance);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //starting background service
        startService(new Intent(this, BackgroundService.class));

        String bank_name = sharedPreferences.getString("bank_name", "Bank Name");
        String mobile_no = sharedPreferences.getString("phone_no", "Mobile no");
        String balance = sharedPreferences.getString(BALANCE, "Balance");

        user_detail_card_bank_name.setText(bank_name);
        user_detail_card_upi_address.setText(mobile_no + getString(R.string.upi_address_postfix));

        //updating main balance
        updateBalance(balance);

        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getRandomColor();

        char bankInitial;
        if (bank_name.length() > 0) {
            bankInitial = bank_name.charAt(0);
        } else {
            // To Do - add crash reporting that bank name is empty
            bankInitial = 'B';
        }
        TextDrawable textDrawable = TextDrawable.builder().buildRound(String.valueOf(bankInitial), color);
        bank_logo.setImageDrawable(textDrawable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(myPref, 0);
        //sharedPreferences.edit().clear().apply();

        user_detail_card_bank_name = (TextView) findViewById(R.id.user_detail_card_bank_name);
        user_detail_card_upi_address = (TextView) findViewById(R.id.user_detail_card_upi_address);
        user_detail_card_balance = (TextView) findViewById(R.id.user_detail_card_balance);
        bank_logo = (ImageView) findViewById(R.id.bank_logo);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectFragment(item);
                        return true;
                    }
                });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = bottomNavigationView.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = bottomNavigationView.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    private void selectFragment(MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.menu_one:
                selectedFragment = new Fragment1();
                break;
            case R.id.menu_two:
                selectedFragment = new Fragment2();
                break;
        }
        mSelectedItem = item.getItemId();
        if (selectedFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


//    public void makeCall(final String ussdCode) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Intent fetchBalanceIntent = new Intent(Intent.ACTION_CALL);
//                //fetchBalanceIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                fetchBalanceIntent.setData(Uri.parse("tel:" + ussdCode + Uri.encode("#")));
//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//
//                //To Do - for the first time after getting permission, fetchBalanceIntent is not starting. Fix this.
//                startActivity(fetchBalanceIntent);
//
//            }
//        }).start();
//    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_get_help) {
            sharedPreferences = getSharedPreferences(myPref, 0);
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "sohaariapps@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from " + sharedPreferences.getString(PHONE_NO, "0123456789").toString());
            intent.putExtra(Intent.EXTRA_TEXT, "My feedback\n");

            startActivity(Intent.createChooser(intent, "Send Email"));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        MainActivity.activityStopped();
//        stopService(new Intent(this, BackgroundService.class));
        super.onDestroy();
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = PACKAGE_NAME + "/" + USSDAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    protected void onResume() {
        super.onResume();
        //MainActivity.activityResumed();
        startService(new Intent(this, BackgroundService.class));

        sharedPreferences = getSharedPreferences(myPref, 0);
        if (!isAccessibilitySettingsOn(getApplicationContext())) {
            {
                startActivity(new Intent(this, AccessibilityNotEnabled.class));
            }
        }
    }

//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

//    public void showDialog() {
//        android.app.FragmentManager manager = getFragmentManager();
//        OtherServicesActivity dialog = new OtherServicesActivity();
//        dialog.show(manager, "dialog");
//    }
}