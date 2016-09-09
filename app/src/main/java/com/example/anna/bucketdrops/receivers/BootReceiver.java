package com.example.anna.bucketdrops.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.anna.bucketdrops.extras.Util;

public class BootReceiver extends BroadcastReceiver {

    public static final String TAG="ANNA";
    public BootReceiver() {
        Log.d(TAG, "BootReceiver: ");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        Util.scheduleAlarm(context);

    }
}
