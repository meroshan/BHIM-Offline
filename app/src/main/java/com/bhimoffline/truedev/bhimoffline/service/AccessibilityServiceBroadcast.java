package com.bhimoffline.truedev.bhimoffline.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bhimoffline.truedev.bhimoffline.activity.MainActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by rahul on 1/27/2017.
 */

public class AccessibilityServiceBroadcast extends Service {
    public static boolean isAccessibilityEnabled() {
        Log.d(TAG, "waiting for user to enable accessibility service");
        try {
            String canonicalName = USSDAccessibilityService.class.getCanonicalName();
            Log.d(TAG, "canonical name = " + canonicalName);
            for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) MainActivity.getInstance().getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
                if (canonicalName.equalsIgnoreCase(runningServiceInfo.service.getClassName())) {
                    Log.d(TAG, "enabled");
                    return true;
                }
                Log.d(TAG, runningServiceInfo.service.getClassName().toString() + " " + canonicalName.equalsIgnoreCase(runningServiceInfo.service.getClassName()));
            }
        } catch (Throwable e) {
            //a.b(e);
        }
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "AccessibilityServiceBroadcast");
        return super.onStartCommand(intent, flags, startId);
    }
}
