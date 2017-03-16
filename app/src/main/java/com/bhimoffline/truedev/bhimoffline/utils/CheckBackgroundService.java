package com.bhimoffline.truedev.bhimoffline.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.bhimoffline.truedev.bhimoffline.activity.MainActivity;
import com.bhimoffline.truedev.bhimoffline.service.BackgroundService;

/**
 * Created by rahul on 1/27/2017.
 */

public class CheckBackgroundService {

    public static boolean isAccessibilityEnabled() {
        try {
            String canonicalName = BackgroundService.class.getCanonicalName();
            Log.d("service1", "canonical name = " + canonicalName);
            for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) MainActivity.getInstance().getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
                Log.d("service2", runningServiceInfo.service.getClassName().toString() + " " + canonicalName.equalsIgnoreCase(runningServiceInfo.service.getClassName()));
                if (canonicalName.equalsIgnoreCase(runningServiceInfo.service.getClassName())) {
                    Log.d("TAG", "BackgroundService is running");
                    return true;
                }
            }
        } catch (Throwable e) {
            //
        }
        return false;
    }
}
