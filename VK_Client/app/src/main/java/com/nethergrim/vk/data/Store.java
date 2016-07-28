package com.nethergrim.vk.data;

import android.support.annotation.WorkerThread;

import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.StartupResponse;

/**
 * Class that will handle all data persistense and mapping. Default implementation is {@link
 * RealmStore} that is Using {@link io.realm.Realm}.
 *
 * Only this class should be used to map and persist web request results.
 *
 * @author andrej on 30.08.15 (c2q9450@gmail.com).
 * All rights reserved.
 */
public interface Store {

    @WorkerThread
    void manage(StartupResponse startupResponse);

    @WorkerThread
    void manage(ListOfFriends listOfFriends, int offset);

    @WorkerThread
    void manage(ListOfUsers listOfUsers);

    @WorkerThread
    void manage(ConversationsUserObject conversationsUserObject, boolean clearDataBeforePersist);

    @WorkerThread
    void manage(ListOfMessages listOfMessages);

    @WorkerThread
    void deleteConversation(long userId, long chatId);

    @WorkerThread
    void markMessagesAsRead(long conversationId, long lastReadMessage);
}
