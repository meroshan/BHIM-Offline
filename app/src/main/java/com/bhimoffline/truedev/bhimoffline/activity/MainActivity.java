package com.bhimoffline.truedev.bhimoffline.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bhimoffline.truedev.bhimoffline.R;
import com.bhimoffline.truedev.bhimoffline.service.BackgroundService;
import com.bhimoffline.truedev.bhimoffline.service.USSDAccessibilityService;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;

import static com.bhimoffline.truedev.bhimoffline.login.LoginActivity.myPref;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BalanceCardFragment.BottomSheetListener {
    public final static String LOGGED = "isLoggedIn";
    public final static String PHONE_NO = "phone_no";
    public final static String BANK_NAME = "bank_name";
    public final static String BALANCE = "balance";
    private static boolean activityVisible;
    private static MainActivity instance;
    //Button update_balance, other_services, get_started_install;
    SharedPreferences sharedPreferences;
    TextView user_detail_card_bank_name;
    TextView user_detail_card_upi_address;
    TextView user_detail_card_balance;
    ImageView bank_logo;
    ImageView share_whatsApp;
    ImageView rate_play_store;
    String TAG = "tag";
    private FirebaseAnalytics mFirebaseAnalytics;
    private BottomSheetLayout bottomSheet;

    public static MainActivity getInstance() {
        return instance;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityStopped() {
        activityVisible = false;
    }

    public void showMenuSheet(int id, String title) {
        MenuSheetView menuSheetView =
                new MenuSheetView(MainActivity.this, MenuSheetView.MenuType.LIST, title, new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        if (bottomSheet.isSheetShowing()) {
                            bottomSheet.dismissSheet();
                        }
                        if (item.getItemId() == R.id.send_money) {
                            showMenuSheet(R.menu.send_money, "Send to");
                        } else if (item.getItemId() == R.id.my_profile) {
                            showMenuSheet(R.menu.my_profile, "My account");
                        } else if (item.getItemId() == R.id.upi_pin) {
                            showMenuSheet(R.menu.upi_pin, "UPI PIN");
                        }
                        return true;
                    }
                });
        menuSheetView.inflateMenu(id);
        bottomSheet.showWithSheetView(menuSheetView);
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomSheet = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        bottomSheet.setPeekOnDismiss(true);

        Fabric.with(this, new Answers(), new Crashlytics());
        Answers.getInstance().logCustom(new CustomEvent("App opened"));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        sharedPreferences = getSharedPreferences(myPref, 0);

//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, sharedPreferences.getString("phone_no", "Phone no not set"));
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, sharedPreferences.getString("bank_name", "No bank set"));
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        user_detail_card_bank_name = (TextView) findViewById(R.id.user_detail_card_bank_name);
        user_detail_card_upi_address = (TextView) findViewById(R.id.user_detail_card_upi_address);
        user_detail_card_balance = (TextView) findViewById(R.id.user_detail_card_balance);
        bank_logo = (ImageView) findViewById(R.id.bank_logo);
        share_whatsApp = (ImageView) findViewById(R.id.share_whatsApp);
        rate_play_store = (ImageView) findViewById(R.id.rate_play_store);
        //get_started_install = (Button) findViewById(R.id.get_started_install);

        instance = this;

        share_whatsApp.setOnClickListener(this);
        rate_play_store.setOnClickListener(this);
        //get_started_install.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Toast.makeText(instance, "saving state", Toast.LENGTH_SHORT).show();
        super.onSaveInstanceState(outState, outPersistentState);
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
        user_detail_card_upi_address.setText(mobile_no + "@upi");

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
        user_detail_card_balance.setText(balance);
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
//            case R.id.get_started_install:
//                Toast.makeText(instance, "Opening Play Store", Toast.LENGTH_SHORT).show();
//                Intent phonePe = new Intent(Intent.ACTION_VIEW);
//                phonePe.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.phonepe.app"));
//                startActivity(phonePe);
//                break;
        }
    }

    private void shareOnWhatsApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String message = "Don't have Internet connection and still want to check your bank balance and send money? Download BHIM Offline" +
                " app and experience offline banking on a click.\n https://play.google.com/store/apps/details?id=com.sohaari.bhimoffline";
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(shareIntent);
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + USSDAccessibilityService.class.getCanonicalName();
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

    public void makeCall(final String ussdCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent fetchBalanceIntent = new Intent(Intent.ACTION_CALL);
                //fetchBalanceIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                fetchBalanceIntent.setData(Uri.parse("tel:" + ussdCode + Uri.encode("#")));
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                //To Do - for the first time after getting permission, fetchBalanceIntent is not starting. Fix this.
                startActivity(fetchBalanceIntent);

            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(instance, "MainActivity onBackPressed", Toast.LENGTH_SHORT).show();
        //super.onBackPressed();
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
        //MainActivity.activityPaused();
        //Toast.makeText(instance, "onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        MainActivity.activityStopped();
        stopService(new Intent(this, BackgroundService.class));
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        MainActivity.activityResumed();
        startService(new Intent(this, BackgroundService.class));

        sharedPreferences = getSharedPreferences(myPref, 0);
        //Toast.makeText(MainActivity.this, "onResume", Toast.LENGTH_SHORT).show();

        // No need to check for if used is logged in or not as MainActivity is started only if used is logged in
        //if (sharedPreferences.getBoolean("isLoggedIn", false) == true) {
        if (!isAccessibilitySettingsOn(getApplicationContext())) {
            {
                startActivity(new Intent(this, AccessibilityNotEnabled.class));
//                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        }
        //} else {
        //    startActivity(new Intent(MainActivity.this, LoginActivity.class));
        //}
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















