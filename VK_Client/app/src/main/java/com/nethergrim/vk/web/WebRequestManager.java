package com.nethergrim.vk.web;

import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.StartupResponse;

import java.util.List;

import retrofit.client.Response;
import rx.Observable;

/**
 * @author andreydrobyazko on 4/3/15.
 */

public interface WebRequestManager {


    Observable<ListOfUsers> getUsers(List<Long> ids);

    Observable<Response> unregisterFromPushNotifications();

    Observable<ListOfFriends> getFriends(long userId, int count, int offset);

    Observable<StartupResponse> launchStartupTasks(String gcmToken);

    Observable<ConversationsUserObject> getConversationsAndUsers(int limit,
            int offset,
            boolean unread);

    Observable<ListOfMessages> getChatHistory(int offset,
            int count,
            long userId,
            long chatId,
            long startMessageId,
            boolean reversedSorting);
}
