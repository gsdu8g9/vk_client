package com.nethergrim.vk.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.models.push.PushMessage;
import com.nethergrim.vk.models.push.PushObject;
import com.nethergrim.vk.utils.PushParser;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.web.WebRequestManager;
import com.vk.sdk.api.VKError;

import java.util.Collections;

import javax.inject.Inject;

import io.realm.Realm;

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

    @Inject
    Realm mRealm;

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
                PushMessage pushMessage = (PushMessage) pushObject;
                showNotification(pushMessage);
                break;
            default:
                // do not supporting other push notifications now
                break;
        }
    }

    private void showNotification(final PushMessage message) {
        User user = mUserProvider.getUser(message.getUid());
        if (user != null) {
            // just show notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setContentTitle(user.getFirstName() + " " + user.getLastName())
                            .setContentText(message.getText());

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(message.getUid().hashCode(), mBuilder.build());
        } else {
            // fetch user from backend
            long userId = Long.parseLong(message.getUid());
            mWebRequestManager.getUsers(Collections.singletonList(userId),
                    new WebCallback<ListOfUsers>() {
                        @Override
                        public void onResponseSucceed(ListOfUsers response) {
                            mRealm.beginTransaction();
                            mRealm.copyToRealmOrUpdate(response.getResponse());
                            mRealm.commitTransaction();
                            showNotification(message);
                        }

                        @Override
                        public void onResponseFailed(VKError e) {
                            Log.e("TAG", "response failed: " + e.errorMessage);
                        }
                    });
        }

    }
}
