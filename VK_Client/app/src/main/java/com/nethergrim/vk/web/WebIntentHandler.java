package com.nethergrim.vk.web;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.nethergrim.vk.models.Conversation;

import java.util.ArrayList;

/**
 * Should be used from Ui Thread, to launch Service which will make web request and persist data..
 * After that on the Ui Thread Subscriber should be notified with {@link com.squareup.otto.Bus}
 * Inside, it will call web requests in {@link android.app.Service} in the background thread, to
 * process and persist all the data.
 *
 * @author andrej on 24.07.15.
 */
public interface WebIntentHandler {

    @UiThread
    void fetchConversationsAndUsers(int count, int offset, boolean onlyUnread);

    @UiThread
    void fetchUsers(@NonNull ArrayList<Long> userIds);

    @UiThread
    void fetchMyFriends(int count, int offset);


    @UiThread
    /**
     * This method will register to push-notifications, register online for 15 minutes, and fetch
     * current user.
     * */
    void launchStartupTasks();

    void fetchMessagesHistory(int count, int offset, String userId, long chatId);

    void deleteConversation(Conversation conversation);

    void fetchStickers();

}
