package com.bhimoffline.truedev.bhimoffline.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by rahul on 16-Mar-17.
 */

public class BackgroundService extends Service {
    String Tag = "bhimService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Tag, "BackgroundService started");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(Tag, "BackgroundService stopped");
        super.onDestroy();
    }
}
