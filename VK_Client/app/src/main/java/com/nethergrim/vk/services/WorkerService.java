package com.nethergrim.vk.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.PendingMessage;
import com.nethergrim.vk.utils.DefaultLoggerObserver;
import com.nethergrim.vk.utils.LoggerObserver;
import com.nethergrim.vk.web.DataManager;

import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 14.08.15.
 */
@SuppressWarnings("UnusedParameters")
public class WorkerService extends Service {

    public static final String TAG = WorkerService.class.getSimpleName();
    private static final String ACTION_FETCH_CONVERSATIONS_AND_USERS = Constants.PACKAGE_NAME + ".FETCH_CONVERSATIONS_AND_USERS";
    private static final String ACTION_FETCH_USERS = Constants.PACKAGE_NAME + ".FETCH_USERS";
    private static final String ACTION_FETCH_MY_FRIENDS = Constants.PACKAGE_NAME + ".FETCH_MY_FRIENDS";
    private static final String ACTION_LAUNCH_STARTUP_TASKS = Constants.PACKAGE_NAME + ".STARTUP";
    private static final String ACTION_FETCH_STICKERS = Constants.PACKAGE_NAME + ".FETCH_STICKERS";
    private static final String ACTION_DELETE_CONVERSATION = Constants.PACKAGE_NAME + ".DELETE_CONVERSATION";
    private static final String ACTION_MARK_MESSAGES_AS_READ = Constants.PACKAGE_NAME + ".MARK_MESSAGES_AS_READ";
    private static final String ACTION_SYNC_MESSAGES_READ_STATE = Constants.PACKAGE_NAME + ".SYNC_READ_STATE";
    private static final String ACTION_GET_MESSAGES_HISTORY = Constants.PACKAGE_NAME + ".GET_MESSAGES_HISTORY";
    private static final String ACTION_SEND_MESSAGE = Constants.PACKAGE_NAME + ".SEND_MESSAGE";
    private static final String EXTRA_IDS = Constants.PACKAGE_NAME + ".IDS";
    private static final String EXTRA_COUNT = Constants.PACKAGE_NAME + ".COUNT";
    private static final String EXTRA_OFFSET = Constants.PACKAGE_NAME + ".OFFSET";
    private static final String EXTRA_ONLY_UNREAD = Constants.PACKAGE_NAME + ".UNREAD_ONLY";
    private static final String EXTRA_USER_ID = Constants.PACKAGE_NAME + ".USER_ID";
    private static final String EXTRA_CHAT_ID = Constants.PACKAGE_NAME + ".CHAT_ID";
    private static final String EXTRA_LAST_MESSAGE = Constants.PACKAGE_NAME + ".LAST_MESSAGE";
    private static final String EXTRA_PEER_ID = Constants.PACKAGE_NAME + ".PEER_ID";
    private static final String EXTRA_PENDING_MESSAGE = Constants.PACKAGE_NAME + ".PENDING_MESSAGE";


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

    public static void fetchStickers(Context context) {
        Intent intent = new Intent(context, WorkerService.class);
        intent.setAction(ACTION_FETCH_STICKERS);
        context.startService(intent);
    }

    public static void markMessagesAsRead(Context c, long conversationId, long lastMessage) {
        Intent intent = new Intent(c, WorkerService.class);
        intent.setAction(ACTION_MARK_MESSAGES_AS_READ);
        intent.putExtra(EXTRA_CHAT_ID, conversationId);
        intent.putExtra(EXTRA_LAST_MESSAGE, lastMessage);
        c.startService(intent);
    }

    public static void syncMessagesReadState(Context c) {
        Intent intent = new Intent(c, WorkerService.class);
        intent.setAction(ACTION_SYNC_MESSAGES_READ_STATE);
        c.startService(intent);
    }

    public static void sendMessage(@NonNull Context c, long peerId, @NonNull PendingMessage pendingMessage) {
        Intent intent = new Intent(c, WorkerService.class);
        intent.setAction(ACTION_SEND_MESSAGE);
        intent.putExtra(EXTRA_PEER_ID, peerId);
        intent.putExtra(EXTRA_PENDING_MESSAGE, pendingMessage);
        c.startService(intent);
    }

    @Inject
    DataManager mDataManager;

    @Inject
    Prefs mPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }
        String action = intent.getAction();
        if (ACTION_FETCH_CONVERSATIONS_AND_USERS.equals(action)) {
            handleActionFetchConversationsAndUsers(intent);
        } else if (ACTION_FETCH_USERS.equals(action)) {
            handleActionFetchUsers(intent);
        } else if (ACTION_FETCH_MY_FRIENDS.equals(action)) {
            handleActionFetchMyFriends(intent);
        } else if (ACTION_LAUNCH_STARTUP_TASKS.equals(action)) {
            handleActionLaunchStartupTasks();
        } else if (ACTION_GET_MESSAGES_HISTORY.equals(action)) {
            handleActionFetchMessagesHistory(intent);
        } else if (ACTION_DELETE_CONVERSATION.equals(action)) {
            handleActionDeleteConversation(intent);
        } else if (ACTION_FETCH_STICKERS.equals(action)) {
            handleActionFetchStickers(intent);
        } else if (ACTION_MARK_MESSAGES_AS_READ.equals(action)) {
            handleActionMarkMessagesAsRead(intent);
        } else if (ACTION_SYNC_MESSAGES_READ_STATE.equals(action)) {
            handleActionSyncMessagesState();
        } else if (ACTION_SEND_MESSAGE.equals(action)) {
            handleActionSendMessage(intent);
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleActionSendMessage(Intent intent) {
        Bundle args = intent.getExtras();
        long peerId = args.getLong(EXTRA_PEER_ID);
        PendingMessage pendingMessage = args.getParcelable(EXTRA_PENDING_MESSAGE);
        assert pendingMessage != null;
        mDataManager.sendMessageOrSchedule(peerId, pendingMessage).subscribe(DefaultLoggerObserver.getInstance());
    }

    private void handleActionMarkMessagesAsRead(Intent intent) {
        Bundle args = intent.getExtras();
        long conversationId = args.getLong(EXTRA_CHAT_ID);
        long toTime = args.getLong(EXTRA_LAST_MESSAGE);
        mDataManager.markMessagesAsRead(conversationId, toTime)
                .subscribe(DefaultLoggerObserver.getInstance());
    }

    private void handleActionSyncMessagesState() {
        mDataManager.syncMessagesReadState().subscribe(DefaultLoggerObserver.getInstance());
    }

    private void handleActionFetchStickers(Intent intent) {
        mDataManager.getStickerItems()
                .subscribe(stickerDbItems -> {
                }, e -> {
                    if (!(e instanceof UnknownHostException)) {
                        Log.e("WebError", e.toString() + " " + e.getMessage());
                        // TODO: 05.09.15 add analytics handling here
                    }
                });
    }

    private void handleActionDeleteConversation(Intent intent) {
        final long userUd = intent.getLongExtra(EXTRA_USER_ID, 0);
        final long chatId = intent.getLongExtra(EXTRA_CHAT_ID, 0);
        mDataManager.deleteConversation(userUd, chatId).subscribe(LoggerObserver.getInstance());
    }

    private void handleActionFetchMessagesHistory(Intent intent) {
        final int count = intent.getIntExtra(EXTRA_COUNT, 10);
        final int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        String userId = intent.getExtras().getString(EXTRA_USER_ID, null);
        long chatId = intent.getExtras().getLong(EXTRA_CHAT_ID, 0);
        mDataManager.fetchMessagesHistory(count, offset, userId, chatId)
                .subscribe(LoggerObserver.getInstance());
    }

    private void handleActionLaunchStartupTasks() {
        mDataManager.launchStartupTasksAndPersistToDb().subscribe(LoggerObserver.getInstance());
    }

    private void handleActionFetchMyFriends(Intent intent) {
        final int count = intent.getIntExtra(EXTRA_COUNT, 10);
        final int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        mDataManager.fetchFriendsAndPersistToDb(count, offset)
                .subscribe(LoggerObserver.getInstance());
    }

    @SuppressWarnings("unchecked")
    private void handleActionFetchUsers(Intent intent) {
        final ArrayList<Long> ids = (ArrayList<Long>) intent.getSerializableExtra(EXTRA_IDS);
        mDataManager.fetchUsersAndPersistToDB(ids).subscribe(LoggerObserver.getInstance());
    }

    private void handleActionFetchConversationsAndUsers(Intent intent) {
        final int limit = intent.getIntExtra(EXTRA_COUNT, 10);
        final int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        final boolean unreadOnly = intent.getBooleanExtra(EXTRA_ONLY_UNREAD, false);
        mDataManager.fetchConversationsUserAndPersist(limit, offset, unreadOnly)
                .subscribe(LoggerObserver.getInstance());
    }

}
