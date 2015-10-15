package com.nethergrim.vk.web;

import android.support.annotation.NonNull;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.services.WorkerService;
import com.nethergrim.vk.utils.ConversationUtils;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Should be used from Ui Thread, to launch Service which will make web request and persist data..
 * After that on the Ui Thread Subscriber should be notified with {@link com.squareup.otto.Bus}
 * Inside, it will call web requests in {@link android.app.Service} in the background thread, to
 * process and persist all the data.
 *
 * @author andrej on 24.07.15.
 */
public class WebIntentHandlerImpl implements WebIntentHandler {

    @Inject
    Prefs mPrefs;

    public WebIntentHandlerImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public void fetchConversationsAndUsers(int count, int offset, boolean onlyUnread) {
        WorkerService.fetchConversationsAndUsers(MyApplication.getInstance(), count, offset,
                onlyUnread);
    }

    @Override
    public void fetchUsers(@NonNull ArrayList<Long> userIds) {
        WorkerService.fetchUsers(MyApplication.getInstance(), userIds);
    }

    @Override
    public void fetchMyFriends(int count, int offset) {
        WorkerService.fetchMyFriends(MyApplication.getInstance(), count, offset);
    }

    @Override
    public void launchStartupTasks() {
        WorkerService.launchStartupTasks(MyApplication.getInstance());
    }

    @Override
    public void fetchMessagesHistory(int count, int offset, String userId, long chatId) {
        WorkerService.fetchMessagesHistory(MyApplication.getInstance(), count, offset, userId,
                chatId);
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
        WorkerService.deleteConversation(MyApplication.getInstance(), userId, chatId);
    }

    @Override
    public void fetchStickers() {
        long previousTimeStamp = mPrefs.getLastFetchStickersTime();
        long minDelay = 1000 * 60 * 60;
        if (previousTimeStamp + minDelay <= System.currentTimeMillis()) {
            WorkerService.fetchStickers(MyApplication.getInstance());
            mPrefs.setLastFetchStickersTime(System.currentTimeMillis());
        }

    }

}
