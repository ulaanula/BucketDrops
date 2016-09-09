package com.example.anna.bucketdrops.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.util.Log;

import com.example.anna.bucketdrops.ActivityMain;
import com.example.anna.bucketdrops.R;
import com.example.anna.bucketdrops.beans.Drop;

import br.com.goncalves.pugnotification.notification.PugNotification;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class NotificationService extends IntentService {

    public static final String TAG ="ANNA";
    public NotificationService() {

        super("NotificationService");
        Log.d(TAG, "NotificationService: ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: ");

        Realm realm =null;
        try{
            realm = Realm.getDefaultInstance();
            RealmResults<Drop> results = realm.where(Drop.class).equalTo("completed",false).findAll();

            for(Drop current: results){

               if(isNotificationNeeded(current.getAdded(),current.getWhen())){
                    fireNotification(current);
               }
            }

        }finally{
            if(realm !=null){
                realm.close();
            }
        }
    }

    private void fireNotification(Drop drop) {

        String message = getString(R.string.notif_message ) + "\"" + drop.getWhat() + "\"" ;

        PugNotification.with(this)
                .load()
                .title(R.string.notif_title)
                .message(message)
                .bigTextStyle(R.string.notif_long_message)
                .smallIcon(R.drawable.ic_drop)
                .largeIcon(R.drawable.ic_drop)
                .flags(Notification.DEFAULT_ALL)
                .click(ActivityMain.class)
                .autoCancel(true)
                .simple()
                .build();
    }

    private boolean isNotificationNeeded(long added, long when){

        long now = System.currentTimeMillis();

        if(now > when){
            return false;
        }else{
            long difference90 = ((long)0.9*(when - now));
            return (now > added+difference90)? true :false;
        }
    }

}
