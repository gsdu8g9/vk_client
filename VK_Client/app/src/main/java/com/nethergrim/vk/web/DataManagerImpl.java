package com.nethergrim.vk.web;

import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.data.PersistingManager;
import com.nethergrim.vk.event.ConversationsUpdatedEvent;
import com.nethergrim.vk.event.FriendsUpdatedEvent;
import com.nethergrim.vk.event.MyUserUpdatedEvent;
import com.nethergrim.vk.event.UsersUpdatedEvent;
import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.StartupResponse;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Class that will handle results of every web request. Should be used to map, persist data, and
 * notify UI thread to be updated.
 * <p>
 * This should be the only way to handle web requests results.
 * <p>
 * By default every method of current class should return {@link rx.Observable} as result.
 *
 * @author andrej on 30.08.15 (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class DataManagerImpl implements DataManager {


    public static final String TAG = DataManager.class.getSimpleName();

    @Inject
    Prefs mPrefs;


    @Inject
    WebRequestManager mWebRequestManager;

    @Inject
    PersistingManager mPersistingManager;

    @Inject
    Bus mBus;

    public DataManagerImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public Observable<StartupResponse> launchStartupTasksAndPersistToDb() {

        // prepare parameters
        String token = mPrefs.getGcmToken();
        if (TextUtils.isEmpty(token)) {
            InstanceID instanceID = InstanceID.getInstance(
                    MyApplication.getInstance().getApplicationContext());
            try {
                token = instanceID.getToken(Constants.GCM_SENDER_ID,
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPrefs.setGcmToken(token);
        }
        return mWebRequestManager
                .launchStartupTasks(token)
                .doOnNext(mPersistingManager::manage)
                .doOnNext(startupResponse -> mBus.post(new MyUserUpdatedEvent()))
                ;
    }

    @Override
    public Observable<ListOfFriends> fetchFriendsAndPersistToDb(int count, int offset) {
        return mWebRequestManager.getFriends(mPrefs.getCurrentUserId(), count, offset)
                .doOnNext(listOfFriends -> mPersistingManager.manage(listOfFriends, offset))
                .doOnNext(listOfFriends -> mBus.post(new FriendsUpdatedEvent(count)))
                ;
    }

    @Override
    public Observable<ListOfUsers> fetchUsersAndPersistToDB(List<Long> ids) {
        return mWebRequestManager.getUsers(ids).doOnNext(mPersistingManager::manage)
                .doOnNext(listOfUsers -> mBus.post(new UsersUpdatedEvent()))
                ;
    }

    @Override
    public Observable<ConversationsUserObject> fetchConversationsUserAndPersist(int limit,
            int offset,
            boolean unreadOnly) {
        return mWebRequestManager.getConversationsAndUsers(limit, offset, unreadOnly)
                .doOnNext(conversationsUserObject -> mPersistingManager.manage(
                        conversationsUserObject, offset == 0))
                .doOnNext(conversationsUserObject -> {
                    mBus.post(new ConversationsUpdatedEvent());
                    mBus.post(new UsersUpdatedEvent());
                });
    }
}
