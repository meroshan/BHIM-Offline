package com.bhimoffline.truedev.bhimoffline.service;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bhimoffline.truedev.bhimoffline.R;
import com.crashlytics.android.Crashlytics;

/**
 * Created by rahul on 1/6/2017.
 */

public class AccessibilityNotEnabled extends Activity {

    Button go_to_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enable_accessibility_service);

        Log.d("tag", "Accessibility Not Enabled called");

        go_to_setting = (Button) findViewById(R.id.go_to_setting);
        go_to_setting.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Crashlytics.logException(e);
                        }
                        finish();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(this, "ANE onBackPressed", Toast.LENGTH_SHORT).show();
        Intent goHome = new Intent(Intent.ACTION_MAIN);
        goHome.addCategory(Intent.CATEGORY_HOME);
        goHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
    }
}
