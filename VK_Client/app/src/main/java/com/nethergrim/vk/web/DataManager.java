package com.nethergrim.vk.web;

import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.IntegerResponse;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.StartupResponse;
import com.nethergrim.vk.models.StickerDbItem;
import com.nethergrim.vk.models.WebResponse;

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
 * @author andrej on 30.08.15 (c2q9450@gmail.com).
 *         All rights reserved.
 */
public interface DataManager {

    Observable<StartupResponse> launchStartupTasksAndPersistToDb();

    Observable<ListOfFriends> fetchFriendsAndPersistToDb(int count, int offset);

    Observable<ListOfUsers> fetchUsersAndPersistToDB(List<Long> ids);

    Observable<ConversationsUserObject> fetchConversationsUserAndPersist(int limit,
            int offset,
            boolean unreadOnly);

    Observable<ListOfMessages> fetchMessagesHistory(int count,
            int offset,
            String userId,
            long chatId);

    Observable<IntegerResponse> deleteConversation(long userId, long chatId);

    Observable<List<StickerDbItem>> getStickerItems();

    Observable<WebResponse> markMessagesAsRead(long conversationsId, long lastMessageId);

    Observable<WebResponse> syncMessagesReadState();
}
