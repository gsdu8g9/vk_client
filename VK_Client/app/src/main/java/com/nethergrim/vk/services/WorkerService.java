package com.nethergrim.vk.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.event.ConversationsUpdatedEvent;
import com.nethergrim.vk.event.FriendsUpdatedEvent;
import com.nethergrim.vk.event.UsersUpdatedEvent;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.DataHelper;
import com.nethergrim.vk.web.WebRequestManager;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * @author andrej on 14.08.15.
 */
public class WorkerService extends Service {

    public static final String TAG = WorkerService.class.getSimpleName();
    public static final String ACTION_FETCH_CONVERSATIONS_AND_USERS = Constants.PACKAGE_NAME
            + ".FETCH_CONVERSATIONS_AND_USERS";
    public static final String ACTION_FETCH_USERS = Constants.PACKAGE_NAME + ".FETCH_USERS";
    public static final String ACTION_FETCH_MY_FRIENDS = Constants.PACKAGE_NAME
            + ".FETCH_MY_FRIENDS";
    public static final String EXTRA_IDS = Constants.PACKAGE_NAME + ".IDS";
    public static final String EXTRA_COUNT = Constants.PACKAGE_NAME + ".COUNT";
    public static final String EXTRA_OFFSET = Constants.PACKAGE_NAME + ".OFFSET";
    public static final String EXTRA_ONLY_UNREAD = Constants.PACKAGE_NAME + ".UNREAD_ONLY";
    public static final int MAX_THREADS_COUNT = 3;
    @Inject
    WebRequestManager mWebRequestManager;

    @Inject
    Prefs mPrefs;

    @Inject
    Bus mBus;
    private ExecutorService mExecutorService;
    private List<Future<?>> mFutures = new ArrayList<>(300);

    public static void fetchConversationsAndUsers(Context context,
            int count,
            int offset,
            boolean unreadOnly) {
        Intent intent = new Intent(context, WorkerService.class);
        intent.setAction(ACTION_FETCH_CONVERSATIONS_AND_USERS);
        intent.putExtra(EXTRA_COUNT, count);
        intent.putExtra(EXTRA_OFFSET, offset);
        intent.putExtra(EXTRA_ONLY_UNREAD, unreadOnly);
        context.startService(intent);
    }

    public static void fetchUsers(Context context, ArrayList<Long> ids) {
        Intent intent = new Intent(context, WorkerService.class);
        intent.setAction(ACTION_FETCH_USERS);
        intent.putExtra(EXTRA_IDS, ids);
        context.startService(intent);
    }

    public static void fetchMyFriends(Context context, int count, int offset) {
        Intent intent = new Intent(context, WorkerService.class);
        intent.setAction(ACTION_FETCH_MY_FRIENDS);
        intent.putExtra(EXTRA_COUNT, count);
        intent.putExtra(EXTRA_OFFSET, offset);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getInstance().getMainComponent().inject(this);
        mExecutorService = Executors.newFixedThreadPool(MAX_THREADS_COUNT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_FETCH_CONVERSATIONS_AND_USERS.equals(intent.getAction())) {
            handleActionFetchConversationsAndUsers(intent);
        } else if (ACTION_FETCH_USERS.equals(intent.getAction())) {
            handleActionFetchUsers(intent);
        } else if (ACTION_FETCH_MY_FRIENDS.equals(intent.getAction())) {
            handleActionFetchMyFriends(intent);
        }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleActionFetchMyFriends(Intent intent) {
        final int count = intent.getIntExtra(EXTRA_COUNT, 10);
        final int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        addRunnableToQueue(new Runnable() {
            @Override
            public void run() {
                ListOfFriends listOfFriends = mWebRequestManager.getFriends(
                        mPrefs.getCurrentUserId(), count, offset);
                if (listOfFriends != null) {

                    mPrefs.setFriendsCount(listOfFriends.getResponse().getCount());

                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    List<User> friends = listOfFriends.getResponse().getFriends();
                    for (int i = 0, size = friends.size(), rating = offset;
                            i < size;
                            i++, rating++) {
                        friends.get(i).setFriendRating(rating);
                    }
                    realm.copyToRealmOrUpdate(friends);
                    realm.commitTransaction();
                    mBus.post(new FriendsUpdatedEvent(listOfFriends.getResponse().getCount()));
                }
            }
        });
    }

    private void handleActionFetchUsers(Intent intent) {
        final ArrayList<Long> ids = (ArrayList<Long>) intent.getSerializableExtra(EXTRA_IDS);
        addRunnableToQueue(new Runnable() {
            @Override
            public void run() {
                ListOfUsers listOfUsers = mWebRequestManager.getUsers(ids);
                if (listOfUsers != null) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(listOfUsers.getResponse());
                    realm.commitTransaction();
                    mBus.post(new UsersUpdatedEvent());
                }
            }
        });
    }

    private void handleActionFetchConversationsAndUsers(Intent intent) {
        final int limit = intent.getIntExtra(EXTRA_COUNT, 10);
        final int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        final boolean unreadOnly = intent.getBooleanExtra(EXTRA_ONLY_UNREAD, false);
        addRunnableToQueue(new Runnable() {
            @Override
            public void run() {
                ConversationsUserObject conversationsUserObject
                        = mWebRequestManager.getConversationsAndUsers(limit, offset, unreadOnly);
                if (conversationsUserObject != null) {

                    //saving conversations to db
                    ConversationsList conversationsList
                            = conversationsUserObject.getResponse().getConversations();
                    conversationsList.setResults(
                            DataHelper.normalizeConversationsList(conversationsList.getResults()));
                    mPrefs.setUnreadMessagesCount(conversationsList.getUnreadCount());
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(conversationsList.getResults());

                    //saving users to db
                    List<User> users = conversationsUserObject.getResponse().getUsers();
                    realm.copyToRealmOrUpdate(users);

                    realm.commitTransaction();
                    mBus.post(new ConversationsUpdatedEvent());
                    mBus.post(new UsersUpdatedEvent());
                } else {
                    Log.e(TAG, "ConversationsUserObject is null. Should not happen.");
                }
            }
        });
    }

    private void addRunnableToQueue(@NonNull Runnable r) {
        if (mExecutorService != null) {
            Future<?> f = mExecutorService.submit(r);
            mFutures.add(f);
        }
    }

    /**
     * @return true if all Futures that are representing web requests are finished and done.
     * This means that current Service can be stopped correctly.
     */
    private boolean canBeKilled() {
        for (int i = 0, size = mFutures.size(); i < size; i++) {
            try {
                if (mFutures.get(i).get() != null) {
                    return false;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

}
