package com.nethergrim.vk.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.web.WebRequestManager;

import javax.inject.Inject;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class MyGcmListenerService extends GcmListenerService {


    @Inject
    WebRequestManager mWebRequestManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "injecting myGcmListenerService");
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String message = data.getString("message");
        Log.e("TAG", "new message");
        Log.e("TAG", "From: " + from);
        Log.e("TAG", "Message: " + message);
        // TODO handle messages receiving
        showNotification(message);

    }

    private void showNotification(String message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_ab_done)
                        .setContentTitle("My notification")
                        .setContentText(message);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
