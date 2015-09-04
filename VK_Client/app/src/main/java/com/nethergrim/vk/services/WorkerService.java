package com.nethergrim.vk.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.web.DataManager;

import java.util.ArrayList;

import javax.inject.Inject;

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
    public static final String ACTION_LAUNCH_STARTUP_TASKS = Constants.PACKAGE_NAME + ".STARTUP";
    public static final String EXTRA_IDS = Constants.PACKAGE_NAME + ".IDS";
    public static final String EXTRA_COUNT = Constants.PACKAGE_NAME + ".COUNT";
    public static final String EXTRA_OFFSET = Constants.PACKAGE_NAME + ".OFFSET";
    public static final String EXTRA_ONLY_UNREAD = Constants.PACKAGE_NAME + ".UNREAD_ONLY";
    public static final int MAX_THREADS_COUNT = 3;

    @Inject
    DataManager mDataManager;


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

    public static void launchStartupTasks(Context context) {
        Intent intent = new Intent(context, WorkerService.class);
        intent.setAction(ACTION_LAUNCH_STARTUP_TASKS);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_FETCH_CONVERSATIONS_AND_USERS.equals(intent.getAction())) {
            handleActionFetchConversationsAndUsers(intent);
        } else if (ACTION_FETCH_USERS.equals(intent.getAction())) {
            handleActionFetchUsers(intent);
        } else if (ACTION_FETCH_MY_FRIENDS.equals(intent.getAction())) {
            handleActionFetchMyFriends(intent);
        } else if (ACTION_LAUNCH_STARTUP_TASKS.equals(intent.getAction())) {
            handleActionLaunchStartupTasks();
        }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleActionLaunchStartupTasks() {
        mDataManager.launchStartupTasksAndPersistToDb().subscribe();
    }

    private void handleActionFetchMyFriends(Intent intent) {
        final int count = intent.getIntExtra(EXTRA_COUNT, 10);
        final int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        mDataManager.fetchFriendsAndPersistToDb(count, offset).subscribe();
    }

    private void handleActionFetchUsers(Intent intent) {
        final ArrayList<Long> ids = (ArrayList<Long>) intent.getSerializableExtra(EXTRA_IDS);
        mDataManager.fetchUsersAndPersistToDB(ids).subscribe();
    }

    private void handleActionFetchConversationsAndUsers(Intent intent) {
        final int limit = intent.getIntExtra(EXTRA_COUNT, 10);
        final int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        final boolean unreadOnly = intent.getBooleanExtra(EXTRA_ONLY_UNREAD, false);
        mDataManager.fetchConversationsUserAndPersist(limit, offset, unreadOnly).subscribe();
    }

}
