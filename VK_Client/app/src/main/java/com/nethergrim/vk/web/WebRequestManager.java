package com.nethergrim.vk.web;

import android.support.annotation.NonNull;

import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.IntegerResponse;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.PendingMessage;
import com.nethergrim.vk.models.StartupResponse;
import com.nethergrim.vk.models.StockItemsResponse;
import com.nethergrim.vk.models.WebResponse;

import java.util.List;

import retrofit.client.Response;
import rx.Observable;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 4/3/15.
 */

@SuppressWarnings("unused")
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
                                              String userId,
                                              long chatId);

    Observable<IntegerResponse> deleteConversation(long userId, long chatId);

    Observable<StockItemsResponse> getStickerStockItems();

    Observable<WebResponse> markMessagesAsRead(long peerId, long startMessageId);

    Observable<WebResponse> sendMessage(long peerId, @NonNull PendingMessage pendingMessage);

}
