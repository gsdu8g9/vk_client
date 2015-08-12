package com.nethergrim.vk.web;

import android.support.annotation.Nullable;
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
    @Nullable
    ConversationsList getConversations(int limit,
            int offset,
            boolean onlyUnread,
            int previewLenght);

    @WorkerThread
    @Nullable
    ListOfUsers getUsers(List<Long> ids);

    @WorkerThread
    @Nullable
    User getCurrentUser();

    @WorkerThread
    boolean registerToPushNotifications(String token);

    @WorkerThread
    boolean unregisterFromPushNotifications();

    @WorkerThread
    @Nullable
    ListOfFriendIds getFriendsList(long userId);

    boolean registerOnline();

    @WorkerThread
    @Nullable
    ListOfMessages getChatHistory(int offset,
            int count,
            long userId,
            long chatId,
            long startMessageId,
            boolean reversedSorting);
}
