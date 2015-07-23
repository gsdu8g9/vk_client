package com.nethergrim.vk.web;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.User;

import java.util.List;

/**
 * @author andreydrobyazko on 4/3/15.
 */

public interface WebRequestManager {

    @UiThread
    void getConversations(int limit,
            int offset,
            boolean onlyUnread,
            int previewLenght,
            final WebCallback<ConversationsList> callback);

    @UiThread
    void getUsers(List<Long> ids,
            List<String> fields,
            String nameCase,
            WebCallback<ListOfUsers> callback);

    @UiThread
    void getUsersForConversations(ConversationsList list, WebCallback<ListOfUsers> callback);

    @UiThread
    void getCurrentUser(WebCallback<User> callback);

    @WorkerThread
    void registerToPushNotifications(String token);

    @UiThread
    void unregisterFromPushNotifications();
}
