package com.example.anna.bucketdrops;

import android.app.Application;

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
}
