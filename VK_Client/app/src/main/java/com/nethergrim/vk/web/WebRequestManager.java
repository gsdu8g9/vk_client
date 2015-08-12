package com.nethergrim.vk.web;

import android.support.annotation.WorkerThread;

import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfFriendIds;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.User;

import java.util.List;

/**
 * @author andreydrobyazko on 4/3/15.
 */

public interface WebRequestManager {

    @WorkerThread
    ConversationsList getConversations(int limit,
            int offset,
            boolean onlyUnread,
            int previewLenght);

    @WorkerThread
    ListOfUsers getUsers(List<Long> ids);

    @WorkerThread
    User getCurrentUser();

    @WorkerThread
    boolean registerToPushNotifications(String token);

    @WorkerThread
    boolean unregisterFromPushNotifications();

    @WorkerThread
    ListOfFriendIds getFriendsList(long userId);

    boolean registerOnline();

    @WorkerThread
    ListOfMessages getChatHistory(int offset,
            int count,
            long userId,
            long chatId,
            long startMessageId,
            boolean reversedSorting);
}
