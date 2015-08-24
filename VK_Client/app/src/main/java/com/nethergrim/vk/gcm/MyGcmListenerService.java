package com.nethergrim.vk.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.models.push.PushMessage;
import com.nethergrim.vk.models.push.PushObject;
import com.nethergrim.vk.utils.PushParser;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.utils.Utils;
import com.nethergrim.vk.web.DataManager;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import javax.inject.Inject;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Inject
    DataManager mDataManager;

    @Inject
    PushParser mPushParser;

    @Inject
    UserProvider mUserProvider;

    @Inject
    ImageLoader mImageLoader;

    @Inject
    Bus mBus;

    @Inject
    Prefs mPrefs;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Log.e("TAG", "message received: " + Utils.convertBundleToJson(data).toString());
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
        mDataManager.fetchConversationsAndUsers(5, 0, false);
    }

    private void showNotification(@NonNull final PushMessage message) {
        User user = mUserProvider.getUser(message.getUid());
        mPrefs.setUnreadMessagesCount(Integer.parseInt(message.getBadge()));
        if (user != null) {
            // just show notification

            final String firstName = user.getFirstName();
            final String lastName = user.getLastName();

            mImageLoader.getUserAvatar(user, new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(MyGcmListenerService.this)
                                    .setSmallIcon(R.drawable.ic_stat_content_mail)
                                    .setLargeIcon(bitmap)
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
            long userId = Long.parseLong(message.getUid());

            // TODO
//            mDataManager.fetchUsers(Collections.singletonList(userId),
//                    new WebCallback<ListOfUsers>() {
//                        @Override
//                        public void onUserLoaded(ListOfUsers response) {
//                            showNotification(message);
//                        }
//
//                        @Override
//                        public void onResponseFailed(VKError e) {
//                        }
//                    });
        }

    }
}
