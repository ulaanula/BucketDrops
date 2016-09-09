package com.example.anna.bucketdrops.extras;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.example.anna.bucketdrops.services.NotificationService;

import java.util.List;

/**
 * Created by Anna on 30.08.2016.
 */
public class Util {

    public static void showViews(List<View> views){

        for(View view: views){
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideViews(List<View> views){

        for(View view: views){
            view.setVisibility(View.GONE);
        }
    }

    public static boolean moreThanJellyBean(){
        return Build.VERSION.SDK_INT>15;
    }

    public static void setBackground(View itemView, Drawable drawable){
        if(moreThanJellyBean()) {
            itemView.setBackground(drawable);
        }else{
            itemView.setBackgroundDrawable(drawable);
        }
    }

    public static void scheduleAlarm(Context context){
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 240000, pendingIntent);
    }
}
