package com.nethergrim.vk.web;

import android.support.annotation.WorkerThread;

import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.StartupResponse;

import java.util.List;

import rx.Observable;

/**
 * @author andreydrobyazko on 4/3/15.
 */

public interface WebRequestManager {


    @WorkerThread
    ListOfUsers getUsers(List<Long> ids);

    @WorkerThread
    Observable<ListOfUsers> getUsersObservable(List<Long> ids);

    @WorkerThread
    boolean unregisterFromPushNotifications();

    @WorkerThread
    ListOfFriends getFriends(long userId, int count, int offset);

    @WorkerThread
    StartupResponse launchStartupTasks(String gcmToken);

    @WorkerThread
    ConversationsUserObject getConversationsAndUsers(int limit, int offset, boolean unread);

    @WorkerThread
    ListOfMessages getChatHistory(int offset,
            int count,
            long userId,
            long chatId,
            long startMessageId,
            boolean reversedSorting);
}
