package com.nethergrim.vk.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.activity.ChatActivity;
import com.nethergrim.vk.activity.MainActivity;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.models.push.PushMessage;
import com.nethergrim.vk.models.push.PushObject;
import com.nethergrim.vk.utils.PushParser;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.utils.Utils;
import com.nethergrim.vk.web.WebIntentHandler;
import com.nethergrim.vk.web.WebRequestManager;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Collections;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class MyGcmListenerService extends GcmListenerService {

    public static final String TAG = MyGcmListenerService.class.getSimpleName();
    public static final String GROUP_MESSAGE = "msg";
    @Inject
    WebIntentHandler mWebIntentHandler;
    @Inject
    PushParser mPushParser;
    @Inject
    UserProvider mUserProvider;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    WebRequestManager mWebRequestManager;
    @Inject
    Bus mBus;
    @Inject
    Prefs mPrefs;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Log.e(TAG, "message received: \n        " + Utils.convertBundleToJson(data).toString());
        MyApplication.getInstance().getMainComponent().inject(this);

        PushObject pushObject = mPushParser.parsePushObject(data);
        handleNotificationForPush(pushObject);
    }

    private void handleNotificationForPush(final PushObject pushObject) {

        switch (pushObject.getPushType()) {
            case Message:
                PushMessage pushMessage = (PushMessage) pushObject;
                showNotification(pushMessage);
                break;
            default:
                // do not supporting other push notifications now
                break;
        }
        updateConversations();
    }

    private void updateConversations() {
        mWebIntentHandler.fetchConversationsAndUsers(5, 0, false);
    }

    private void showNotification(@NonNull final PushMessage message) {
        User user = mUserProvider.getUser(message.getUid());
        mPrefs.setUnreadMessagesCount(Integer.parseInt(message.getBadge()));
        if (user != null) {
            // just show notification, we have a user

            final String firstName = user.getFirstName();
            final String lastName = user.getLastName();

            mImageLoader.getUserAvatar(user, new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    // Create an intent for the reply action
                    Intent actionIntent = ChatActivity.getIntentForReplyAction(MyGcmListenerService.this, message);
                    Intent actionMainActivityIntent = new Intent(MyGcmListenerService.this,
                            MainActivity.class);
                    PendingIntent actionPendingIntent =
                            PendingIntent.getActivity(MyGcmListenerService.this, 0, actionIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                    PendingIntent actionMainActivityPendingIntent = PendingIntent.getActivity(MyGcmListenerService.this,0, actionMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // Create the action to reply
                    NotificationCompat.Action action =
                            new NotificationCompat.Action.Builder(R.drawable.ic_stat_content_reply,
                                    getString(R.string.reply_to), actionPendingIntent)
                                    .build();

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(MyGcmListenerService.this)
                                    .setSmallIcon(R.drawable.ic_stat_content_mail)
                                    .setLargeIcon(bitmap)
                                    .setGroup(GROUP_MESSAGE)
                                    .addAction(action)
                                    .setAutoCancel(true)
                                    .setContentIntent(actionMainActivityPendingIntent)
                                    .setContentTitle(firstName + " " + lastName)
                                    .setContentText(message.getText());

                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(message.getUid().hashCode(), mBuilder.build());
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        } else {
            // fetch user from backend
            mWebRequestManager
                    .getUsersObservable(Collections.singletonList(message.getUserId()))
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(listOfUsers -> {
                        // TODO: 30.08.15 persist user to db, and do all mapping stuff
                    }, throwable -> {
                        Log.e(TAG, throwable.toString());
                        // just ignore
                    });
        }

    }
}
