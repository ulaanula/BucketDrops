package com.example.anna.bucketdrops;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.anna.bucketdrops.adapters.Filter;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Anna on 26.08.2016.
 */
public class AppBucketDrops extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }

    //method for Shared Preferences; save
    public static void save(Context context, int filterOption){

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("filter", filterOption);
        editor.apply();
    }
    //method for Shared Preferences
    public static int load(Context context){

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int filterOption = pref.getInt("filter", Filter.NONE);
        return filterOption;
    }

}
