package com.nethergrim.vk.web;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.PendingMessage;
import com.nethergrim.vk.services.WorkerService;
import com.nethergrim.vk.utils.ConversationUtils;

import java.util.ArrayList;

/**
 * Should be used from Ui Thread, to launch Service which will make web request and persist data..
 * After that on the Ui Thread Subscriber should be notified with {@link com.squareup.otto.Bus}
 * Inside, it will call web requests in {@link android.app.Service} in the background thread, to
 * process and persist all the data.
 *
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 24.07.15.
 */
public class WebIntentHandlerImpl implements WebIntentHandler {


    private static final String TAG = "WebIntentHandlerImpl";
    private Prefs mPrefs;
    private Context mContext;

    public WebIntentHandlerImpl(Prefs prefs, Context context) {
        mPrefs = prefs;
        mContext = context;
    }

    @Override
    public void fetchConversationsAndUsers(int count, int offset, boolean onlyUnread) {
        WorkerService.fetchConversationsAndUsers(mContext, count, offset, onlyUnread);
    }

    @Override
    public void fetchUsers(@NonNull ArrayList<Long> userIds) {
        WorkerService.fetchUsers(mContext, userIds);
    }

    @Override
    public void fetchMyFriends(int count, int offset) {
        WorkerService.fetchMyFriends(mContext, count, offset);
    }

    @Override
    public void launchStartupTasks() {
        WorkerService.launchStartupTasks(mContext);
    }

    @Override
    public void fetchMessagesHistory(int count, int offset, String userId, long chatId) {
        WorkerService.fetchMessagesHistory(mContext, count, offset, userId, chatId);
    }

    @Override
    public void deleteConversation(Conversation conversation) {
        boolean groupChat = ConversationUtils.isConversationAGroupChat(conversation);
        long userId = 0;
        long chatId = 0;
        if (groupChat) {
            chatId = conversation.getId();
        } else {
            userId = conversation.getId();
        }
        WorkerService.deleteConversation(mContext, userId, chatId);
    }

    @Override
    public void fetchStickers() {
        WorkerService.fetchStickers(mContext);
        mPrefs.setLastFetchStickersTime(System.currentTimeMillis());
    }

    @Override
    public void markMessagesAsRead(long conversationId, long lastMessageId) {
        WorkerService.markMessagesAsRead(mContext, conversationId, lastMessageId);
    }

    @Override
    public void syncUnreadMessages() {
        WorkerService.syncMessagesReadState(mContext);
    }

    @Override
    public void sendMessageToUser(@NonNull PendingMessage pendingMessage) {
        WorkerService.sendMessage(mContext, pendingMessage.getPeerId(), pendingMessage);
    }

    @Override
    public void sendMessageToGroupChat(@NonNull PendingMessage pendingMessage) {
        WorkerService.sendMessage(mContext, pendingMessage.getPeerId(), pendingMessage);
    }

}
