package com.nethergrim.vk.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.models.push.PushMessage;
import com.nethergrim.vk.models.push.PushObject;
import com.nethergrim.vk.utils.PushParser;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.web.WebRequestManager;

import javax.inject.Inject;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Inject
    WebRequestManager mWebRequestManager;

    @Inject
    PushParser mPushParser;

    @Inject
    UserProvider mUserProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        PushObject pushObject = mPushParser.parsePushObject(data);
        handleNotificationForPush(pushObject);
    }

    private void handleNotificationForPush(PushObject pushObject) {
        switch (pushObject.getPushType()) {
            case Message:

                break;
            default:
                break;
        }
    }

    private void showNotification(PushMessage message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_ab_done)
                        .setContentTitle("My notification")
                        .setContentText(message.getText());

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
