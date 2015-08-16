package com.nethergrim.vk.web;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.StartupResponse;

import java.util.List;

/**
 * @author andreydrobyazko on 4/3/15.
 */

public interface WebRequestManager {


    @WorkerThread
    @Nullable
    ListOfUsers getUsers(List<Long> ids);

    @WorkerThread
    boolean unregisterFromPushNotifications();

    @WorkerThread
    ListOfFriends getFriends(long userId, int count, int offset);

    @WorkerThread
    StartupResponse launchStartupTasks(String gcmToken);

    @WorkerThread
    ConversationsUserObject getConversationsAndUsers(int limit, int offset, boolean unread);

    @WorkerThread
    @Nullable
    ListOfMessages getChatHistory(int offset,
            int count,
            long userId,
            long chatId,
            long startMessageId,
            boolean reversedSorting);
}
