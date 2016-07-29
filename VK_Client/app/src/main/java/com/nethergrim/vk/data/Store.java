package com.nethergrim.vk.data;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.PendingMessage;
import com.nethergrim.vk.models.StartupResponse;

import java.util.List;

/**
 * Class that will handle all data persistense and mapping. Default implementation is {@link
 * RealmStore} that is Using {@link io.realm.Realm}.
 * <p>
 * Only this class should be used to map and persist web request results.
 *
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 30.08.15 (c2q9450@gmail.com).
 *         All rights reserved.
 */
public interface Store {

    @WorkerThread
    void persist(StartupResponse startupResponse);

    @WorkerThread
    void persist(ListOfFriends listOfFriends, int offset);

    @WorkerThread
    void persist(ListOfUsers listOfUsers);

    @WorkerThread
    void persist(ConversationsUserObject conversationsUserObject, boolean clearDataBeforePersist);

    @WorkerThread
    void persist(ListOfMessages listOfMessages);

    @WorkerThread
    void deleteConversation(long userId, long chatId);

    @WorkerThread
    void markMessagesAsRead(long conversationId, long lastReadMessage);

    /**
     * Should immediately save outgoing message, to display in list.
     *
     * @param peerId         user id or conversation id.
     * @param pendingMessage message that should be saved.
     */
    @WorkerThread
    void savePendingMessage(long peerId, @NonNull PendingMessage pendingMessage);

    /**
     * @return list of unsent {@link PendingMessage} that should be sent when internet connection appears.
     */
    @WorkerThread
    List<PendingMessage> getUnsentMessages();

    /**
     * @param peerId   id of user or id of conversation.
     * @param randomId random ID generated per each outgoing message
     */
    @WorkerThread
    void removePendingMessage(long peerId, long randomId);
}
