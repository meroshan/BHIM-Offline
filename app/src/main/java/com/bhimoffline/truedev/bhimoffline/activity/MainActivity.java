package com.bhimoffline.truedev.bhimoffline.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bhimoffline.truedev.bhimoffline.R;
import com.bhimoffline.truedev.bhimoffline.login.LoginActivity1;
import com.bhimoffline.truedev.bhimoffline.service.AccessibilityNotEnabled;
import com.bhimoffline.truedev.bhimoffline.service.USSDAccessibilityService;
import com.bhimoffline.truedev.bhimoffline.utils.SwipeDismissTouchListener;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;

import static com.bhimoffline.truedev.bhimoffline.login.LoginActivity1.myPref;


//<color name="colorPrimary">#629f1d</color>

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static boolean activityVisible;
    private static MainActivity instance;
    Button update_balance, other_services, login;
    SharedPreferences sharedPreferences;
    TextView user_detail_card_bank_name;
    TextView user_detail_card_upi_address;
    TextView user_detail_card_balance;
    TextView balance_card_balance;
    TextView balance_card_last_updated;
    ImageView bank_logo;
    ImageView share_whatsApp;
    ImageView rate_play_store;

    String TAG = "tag";
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
    private FirebaseAnalytics mFirebaseAnalytics;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fabric.with(this, new Answers(), new Crashlytics());
        Answers.getInstance().logCustom(new CustomEvent("App opened"));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        sharedPreferences = getSharedPreferences(myPref, 0);
//        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
//            if (!isAccessibilitySettingsOn(getApplicationContext())) {
//
//                startActivity(new Intent(this, AccessibilityNotEnabled.class));
////                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME));
//            }
//        } else {
//            startActivity(new Intent(MainActivity.this, LoginActivity1.class));
//        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, sharedPreferences.getString("phone_no", "Phone no not set"));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, sharedPreferences.getString("bank_name", "No bank set"));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        user_detail_card_bank_name = (TextView) findViewById(R.id.user_detail_card_bank_name);
        user_detail_card_upi_address = (TextView) findViewById(R.id.user_detail_card_upi_address);
        user_detail_card_balance = (TextView) findViewById(R.id.user_detail_card_balance);
        balance_card_balance = (TextView) findViewById(R.id.balance_card_balance);
        balance_card_last_updated = (TextView) findViewById(R.id.balance_card_last_updated);
        bank_logo = (ImageView) findViewById(R.id.bank_logo);
        share_whatsApp = (ImageView) findViewById(R.id.share_whatsApp);
        rate_play_store = (ImageView) findViewById(R.id.rate_play_store);

        user_detail_card_bank_name.setText(sharedPreferences.getString("bank_name", "Your Bank"));
        user_detail_card_upi_address.setText(sharedPreferences.getString("phone_no", "phone_no") + "@upi");

        final CardView cardView = (CardView) findViewById(R.id.swipable);
        cardView.setOnTouchListener(new SwipeDismissTouchListener(cardView, null,
                new SwipeDismissTouchListener.DismissCallbacks() {

                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view, Object token) {
                        //Toast.makeText(MainActivity.this, "dismissing", Toast.LENGTH_SHORT).show();
                        ((ViewGroup) cardView.getParent()).removeView(cardView);
                    }
                }));

        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getRandomColor();

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(String.valueOf(user_detail_card_bank_name.getText().charAt(0)), color);
        bank_logo.setImageDrawable(textDrawable);

        instance = this;

        update_balance = (Button) findViewById(R.id.update_balance);
        update_balance.setOnClickListener(this);
        other_services = (Button) findViewById(R.id.other_services);
        other_services.setOnClickListener(this);
        share_whatsApp.setOnClickListener(this);
        rate_play_store.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Toast.makeText(instance, "onStart", Toast.LENGTH_SHORT).show();
        user_detail_card_balance.setText(sharedPreferences.getString("balance", "Balance"));
        balance_card_balance.setText(sharedPreferences.getString("balance", "Balance"));
        balance_card_last_updated.setText(sharedPreferences.getString("last_updated", "never"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_balance:
                askForPermission();
                makeCall("*111");
                break;
            case R.id.other_services:
                askForPermission();
                makeCall("*111");
                break;
            case R.id.share_whatsApp:
                Toast.makeText(instance, "Saare friends ko share karo BC", Toast.LENGTH_SHORT).show();
                shareOnWhatsApp();
                break;
            case R.id.rate_play_store:
                Toast.makeText(instance, "5 hi rate karna BC", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void shareOnWhatsApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String message = "download BHIM Offline and see your bank balance without Internet";
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(shareIntent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Boolean temp = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CALL_PHONE);
            showMessage();
            return;
        }
    }

    private void showMessage() {
        AlertDialog.Builder alertDialoge = new AlertDialog.Builder(MainActivity.this);
        alertDialoge.setTitle("Permission Required")
                .setCancelable(false)
                .setMessage("BHIMOfffline doen't work without phone permission\nWe just need to make a call to fetch your balance. So please allow this permission.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialoge.show();
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

    public void makeCall(String ussdCode) {
        Intent fetchBalanceIntent = new Intent(Intent.ACTION_CALL);
        fetchBalanceIntent.setData(Uri.parse("tel:" + ussdCode + Uri.encode("#")));
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(fetchBalanceIntent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(instance, "Maniactivity onBackPressed", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.action_settings) {
            return true;
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
        MainActivity.activityStopped();
        //  Toast.makeText(instance, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        //  activityNo = 1;
        MainActivity.activityResumed();

        sharedPreferences = getSharedPreferences(myPref, 0);
        //Toast.makeText(MainActivity.this, "onResume", Toast.LENGTH_SHORT).show();

        if (sharedPreferences.getBoolean("isLoggedIn", false) == true) {
            //Toast.makeText(MainActivity.this, sharedPreferences.getBoolean("isLoggedIn", false) + "", Toast.LENGTH_SHORT).show();
            if (!isAccessibilitySettingsOn(getApplicationContext())) {
                //Toast.makeText(instance, "Resume", Toast.LENGTH_SHORT).show();
                {
                    // Toast.makeText(instance, "from resume " + activityNo, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, AccessibilityNotEnabled.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY));
                }
            } else {
                return;
            }
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity1.class));
        }
    }
}