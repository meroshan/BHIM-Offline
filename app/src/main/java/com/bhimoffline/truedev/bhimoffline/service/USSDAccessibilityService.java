package com.bhimoffline.truedev.bhimoffline.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.bhimoffline.truedev.bhimoffline.activity.MainActivity.isActivityVisible;

/**
 * Created by rahul on 1/2/2017.
 */

public class USSDAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent");

        AccessibilityNodeInfo source = event.getSource();
    /* if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && !event.getClassName().equals("android.app.AlertDialog")) { // android.app.AlertDialog is the standard but not for all phones  */
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && !String.valueOf(event.getClassName()).contains("AlertDialog")) {
            return;
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && (source == null || !source.getClassName().equals("android.widget.TextView"))) {
            return;
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && TextUtils.isEmpty(source.getText())) {
            return;
        }

        List<CharSequence> eventText;

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            eventText = event.getText();
        } else {
            eventText = Collections.singletonList(source.getText());
        }

        String text = processUSSDText(eventText);
        if (TextUtils.isEmpty(text)) return;

        if (isActivityVisible()) {
            //Toast.makeText(this, "fine", Toast.LENGTH_SHORT).show();
            performGlobalAction(GLOBAL_ACTION_BACK); // This works on 4.1+ only
        } else {
            //Toast.makeText(this, "outside app", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent();
        intent.setAction("truedev.bankbalance.USSD");
        intent.putExtra("message", text);
        sendBroadcast(intent);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    private String processUSSDText(List<CharSequence> eventText) {
        for (CharSequence s : eventText) {
            String text = String.valueOf(s).toLowerCase();
            if (text.contains("rs")) {
                return text;
            }
        }
        return null;
    }

    @Override
    public void onInterrupt() {

    }
}
