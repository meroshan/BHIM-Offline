package com.bhimoffline.truedev.bhimoffline.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.bhimoffline.truedev.bhimoffline.R;

/**
 * Created by rahul on 1/6/2017.
 */

public class AccessibilityNotEnabled extends Activity {

    Button go_to_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enable_accessibility_service);

        go_to_setting = (Button) findViewById(R.id.go_to_setting);
        go_to_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
                //Toast.makeText(AccessibilityNotEnabled.this, "Destroying activity", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });
    }
}
