package com.nethergrim.vk.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.utils.LoggerObserver;
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
    public static final String ACTION_DELETE_CONVERSATION = Constants.PACKAGE_NAME
            + ".DELETE_CONVERSATION";
    public static final String EXTRA_IDS = Constants.PACKAGE_NAME + ".IDS";
    public static final String EXTRA_COUNT = Constants.PACKAGE_NAME + ".COUNT";
    public static final String EXTRA_OFFSET = Constants.PACKAGE_NAME + ".OFFSET";
    public static final String EXTRA_ONLY_UNREAD = Constants.PACKAGE_NAME + ".UNREAD_ONLY";
    public static final String EXTRA_USER_ID = Constants.PACKAGE_NAME + ".USER_ID";
    public static final String EXTRA_CHAT_ID = Constants.PACKAGE_NAME + ".CHAT_ID";
    public static final String ACTION_GET_MESSAGES_HISTORY = Constants.PACKAGE_NAME
            + ".GET_MESSAGES_HISTORY";
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

    public static void fetchMessagesHistory(Context context,
            int count,
            int offset,
            String userId,
            long chatId) {
        Intent intent = new Intent(context, WorkerService.class);
        intent.setAction(ACTION_GET_MESSAGES_HISTORY);
        intent.putExtra(EXTRA_COUNT, count);
        intent.putExtra(EXTRA_OFFSET, offset);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_CHAT_ID, chatId);
        context.startService(intent);
    }

    public static void deleteConversation(Context context, final long userId, final long chatId) {
        Intent intent = new Intent(context, WorkerService.class);
        intent.setAction(ACTION_DELETE_CONVERSATION);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_CHAT_ID, chatId);
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
        } else if (ACTION_GET_MESSAGES_HISTORY.equals(intent.getAction())) {
            handleActionFetchMessagesHistory(intent);
        } else if (ACTION_DELETE_CONVERSATION.equals(intent.getAction())) {
            handleActionDeleteConversation(intent);
        }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleActionDeleteConversation(Intent intent) {
        final long userUd = intent.getLongExtra(EXTRA_USER_ID, 0);
        final long chatId = intent.getLongExtra(EXTRA_CHAT_ID, 0);
        mDataManager.deleteConversation(userUd, chatId).subscribe(new LoggerObserver());
    }

    private void handleActionFetchMessagesHistory(Intent intent) {
        final int count = intent.getIntExtra(EXTRA_COUNT, 10);
        final int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        String userId = intent.getExtras().getString(EXTRA_USER_ID, null);
        long chatId = intent.getExtras().getLong(EXTRA_CHAT_ID, 0);
        mDataManager.fetchMessagesHistory(count, offset, userId, chatId)
                .subscribe(new LoggerObserver());
    }

    private void handleActionLaunchStartupTasks() {
        mDataManager.launchStartupTasksAndPersistToDb().subscribe(new LoggerObserver());
    }

    private void handleActionFetchMyFriends(Intent intent) {
        final int count = intent.getIntExtra(EXTRA_COUNT, 10);
        final int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        mDataManager.fetchFriendsAndPersistToDb(count, offset).subscribe(new LoggerObserver());
    }

    private void handleActionFetchUsers(Intent intent) {
        final ArrayList<Long> ids = (ArrayList<Long>) intent.getSerializableExtra(EXTRA_IDS);
        mDataManager.fetchUsersAndPersistToDB(ids).subscribe(new LoggerObserver());
    }

    private void handleActionFetchConversationsAndUsers(Intent intent) {
        final int limit = intent.getIntExtra(EXTRA_COUNT, 10);
        final int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        final boolean unreadOnly = intent.getBooleanExtra(EXTRA_ONLY_UNREAD, false);
        mDataManager.fetchConversationsUserAndPersist(limit, offset, unreadOnly)
                .subscribe(new LoggerObserver());
    }

}
