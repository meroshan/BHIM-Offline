package com.bhimoffline.truedev.bhimoffline.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.bhimoffline.truedev.bhimoffline.activity.MainActivity;
import com.bhimoffline.truedev.bhimoffline.utils.CheckBackgroundService;

import java.util.Collections;
import java.util.List;

import static com.bhimoffline.truedev.bhimoffline.activity.MainActivity.isActivityVisible;

/**
 * Created by rahul on 1/2/2017.
 */

public class USSDAccessibilityService extends AccessibilityService {
    String TAG = "service";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent");

        AccessibilityNodeInfo source = event.getSource();
        //if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && !event.getClassName().equals("android.app.AlertDialog"))
        // android.app.AlertDialog is the standard but not for all phones
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
            performGlobalAction(GLOBAL_ACTION_BACK); // This works on 4.1+ only
        } else {
            // To DO //////////////////////////////////////////////////////////////////////////////////////////////
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

        if (CheckBackgroundService.isAccessibilityEnabled()) {
            Intent intent = new Intent(this, MainActivity.class);
            //addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
                Log.d(TAG, "opening main activity");
                Toast.makeText(this, "service connected", Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
            } catch (ActivityNotFoundException e2) {
            }
        }

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
            if (text.toLowerCase().contains("rs") && text.contains("your") && text.contains("balance")) {
                return text;
            }
        }
        return null;
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public boolean onUnbind(Intent intent) {
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_call_colored)
//                        .setContentTitle("BHiM Offline is disabled")
//                        .setContentText("Please turn it on!");
//
//        Intent resultIntent = new Intent(this, AccessibilityNotEnabled.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(pendingIntent);
//
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, mBuilder.build());
        //Toast.makeText(this, "disabled", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    // called when user just enables the accessibility service
//    public boolean isAccessibilityEnabled() {
//        Log.d(TAG, "waiting for user to enable accessibility service");
//        try {
//            String canonicalName = USSDAccessibilityService.class.getCanonicalName();
//            Log.d(TAG, "canonical name = " + canonicalName);
//            for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) MainActivity.getInstance().getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
//                if (canonicalName.equalsIgnoreCase(runningServiceInfo.service.getClassName())) {
//                    Log.d(TAG, "enabled");
//                    return true;
//                }
//                Log.d(TAG, runningServiceInfo.service.getClassName().toString() + " " + canonicalName.equalsIgnoreCase(runningServiceInfo.service.getClassName()));
//            }
//        } catch (Throwable e) {
//            //a.b(e);
//        }
//        return false;
//    }
}
