package com.bhimoffline.truedev.bhimoffline.activity;

import android.Manifest;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    public static void activityPaused() {
        //activityVisible = false;
    }

    public static void activityStopped() {
        activityVisible = false;
    }

    protected void onResume() {
        super.onResume();
        MainActivity.activityResumed();

        sharedPreferences = getSharedPreferences(myPref, 0);
        //Toast.makeText(MainActivity.this, "onResume", Toast.LENGTH_SHORT).show();

        if (sharedPreferences.getBoolean("isLoggedIn", false) == true) {
            //Toast.makeText(MainActivity.this, sharedPreferences.getBoolean("isLoggedIn", false) + "", Toast.LENGTH_SHORT).show();
            if (!isAccessibilitySettingsOn(getApplicationContext())) {
                startActivity(new Intent(this, AccessibilityNotEnabled.class).setFlags(268435456));
            } else {
                return;
            }
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity1.class));
        }
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
        // Toast.makeText(instance, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Answers(), new Crashlytics());
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Answers.getInstance().logCustom(new CustomEvent("App opened"));
        //finish();

        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(myPref, 0);
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            if (!isAccessibilitySettingsOn(getApplicationContext())) {
                startActivity(new Intent(this, AccessibilityNotEnabled.class).setFlags(268435456));
            }
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity1.class));
        }

//        if (!hasPermissions(this, PERMISSIONS)) {
//            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//        }

        user_detail_card_bank_name = (TextView) findViewById(R.id.user_detail_card_bank_name);
        user_detail_card_upi_address = (TextView) findViewById(R.id.user_detail_card_upi_address);
        user_detail_card_balance = (TextView) findViewById(R.id.user_detail_card_balance);
        balance_card_balance = (TextView) findViewById(R.id.balance_card_balance);
        balance_card_last_updated = (TextView) findViewById(R.id.balance_card_last_updated);
        bank_logo = (ImageView) findViewById(R.id.bank_logo);

        user_detail_card_bank_name.setText(sharedPreferences.getString("bank_name", "your bank name"));
        user_detail_card_upi_address.setText(sharedPreferences.getString("phone_no", "phone_no") + "@upi");
        user_detail_card_balance.setText(sharedPreferences.getString("balance", ""));
        balance_card_balance.setText(sharedPreferences.getString("balance", "Balance"));
        balance_card_last_updated.setText(sharedPreferences.getString("last_updated", "never"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        instance = this;
        update_balance = (Button) findViewById(R.id.update_balance);
        other_services = (Button) findViewById(R.id.other_services);
        login = (Button) findViewById(R.id.login);

        update_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sharedPreferences.edit().clear().commit();
//                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                askForPermission();
                makeCall("*123");
            }
        });

        other_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermission();
                makeCall("*123");
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity1.class));
            }
        });
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
                        System.exit(0);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
