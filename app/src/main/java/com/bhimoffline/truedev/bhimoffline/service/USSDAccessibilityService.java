package com.bhimoffline.truedev.bhimoffline.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bhimoffline.truedev.bhimoffline.activity.MainActivity;
import com.bhimoffline.truedev.bhimoffline.fragment.BalanceCardFragment;
import com.bhimoffline.truedev.bhimoffline.utils.CheckBackgroundService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.bhimoffline.truedev.bhimoffline.activity.MainActivity.isActivityVisible;

/**
 * Created by rahul on 1/2/2017.
 */

public class USSDAccessibilityService extends AccessibilityService {
    //String TAG = "AccessibilityService";
    String Tag = "bhimService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(Tag, "onAccessibilityEvent called");

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

        String text = eventText.toString();
        Log.d("onAccessibilityEvent", text);

        if (TextUtils.isEmpty(text)) return;

        if (isActivityVisible()) {
            Log.d(Tag, "activity is visible");
            performGlobalAction(GLOBAL_ACTION_BACK); // This works on 4.1+ only
        }

        parseMobileBalance(eventText.toString());
    }

    private void parsebankBalance(String message) {
        String tokens[] = message.split("\\s");

    }

    private void parseMobileBalance(String message) {
        String words[] = message.toLowerCase().split("\\s*[:,.\\s*]\\s*");
        ArrayList<String> keywords = new ArrayList<>(Arrays.asList(words));

        int pos;
        String balance = "Unable to fetch balance";
        if (keywords.contains("rs")) {
            pos = keywords.indexOf("rs");
            if (pos + 1 < keywords.size() && keywords.get(pos + 1).matches("\\d+")) {
                balance = keywords.get(pos + 1);
            } else if (pos - 1 > 0 && keywords.get(pos - 1).matches("\\d+")) {
                balance = keywords.get(pos - 1);
            }
        }
        EventBus.getDefault().post(new BalanceCardFragment.MessageEvent(balance));
        //Toast.makeText(this, "balance is + " + balance, Toast.LENGTH_SHORT).show();
    }

    private void formatBalance(String balance) {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(Tag, "USSDAccessibilityService connected");

        if (CheckBackgroundService.isAccessibilityEnabled(getApplicationContext())) {
            Intent intent = new Intent(this, MainActivity.class);
            //addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
                Log.d(Tag, "Service connected");
                //Toast.makeText(this, "service connected", Toast.LENGTH_SHORT).show();
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
        Log.d(Tag, "USSDAccessibilityService disconnected");
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

//    public interface UpdateBalanceListener {
//        void updateBalance(String balance);
//    }

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
