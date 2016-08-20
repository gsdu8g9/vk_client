package com.nethergrim.vk.web;

import android.support.annotation.NonNull;

import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.IntegerResponse;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.PendingMessage;
import com.nethergrim.vk.models.StartupResponse;
import com.nethergrim.vk.models.StickersCollectionLocal;
import com.nethergrim.vk.models.WebResponse;
import com.nethergrim.vk.models.response.SendMessageResponse;

import java.util.List;

import rx.Observable;

/**
 * Class that will handle results of every web request. Should be used to map, persist data, and
 * notify UI thread to be updated.
 * <p>
 * This should be the only way to handle web requests results.
 * <p>
 * By default every method of current class should return {@link rx.Observable} as result.
 *
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 30.08.15 (c2q9450@gmail.com).
 *         All rights reserved.
 */
public interface DataManager {

    @NonNull
    Observable<StartupResponse> launchStartupTasksAndPersistToDb();

    @NonNull
    Observable<ListOfFriends> fetchFriendsAndPersistToDb(int count, int offset);

    @NonNull
    Observable<ListOfUsers> fetchUsersAndPersistToDB(List<Long> ids);

    @NonNull
    Observable<ConversationsUserObject> fetchConversationsUserAndPersist(int limit,
                                                                         int offset,
                                                                         boolean unreadOnly);

    @NonNull
    Observable<ListOfMessages> fetchMessagesHistory(int count,
                                                    int offset,
                                                    String userId,
                                                    long chatId);

    @NonNull
    Observable<IntegerResponse> deleteConversation(long userId, long chatId);

    @NonNull
    Observable<List<StickersCollectionLocal>> fetchAndPersistStickers();

    @NonNull
    Observable<WebResponse> markMessagesAsRead(long conversationsId, long lastMessageId);

    @NonNull
    Observable<WebResponse> syncMessagesReadState();


    /**
     * Should call place PendingMessage to Store, and sync unsent messages (immediately, if there is an internet connection)
     *
     * @param peerId         id of user or id of conversation
     * @param pendingMessage outgoing message
     * @return web response (if any)
     */
    @NonNull
    Observable<SendMessageResponse> sendMessageOrSchedule(long peerId, @NonNull PendingMessage pendingMessage);


    /**
     * Should send all unsent pending messages in background;
     */
    @NonNull
    Observable<SendMessageResponse> syncPendingMessages();
}
