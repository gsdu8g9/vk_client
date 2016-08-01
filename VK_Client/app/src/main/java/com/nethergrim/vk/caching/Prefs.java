package com.nethergrim.vk.caching;

import android.support.annotation.WorkerThread;

import java.util.Set;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 3/20/15.
 */
@SuppressWarnings("unused")
public interface Prefs {

    long getCurrentUserId();

    void setCurrentUserId(long userId);

    String getToken();

    void setToken(String token);

    int getCurrentActivityStateId();

    void setCurrentActivityStateId(int id);

    String getGcmToken();

    void setGcmToken(String token);

    int getUnreadMessagesCount();

    void setUnreadMessagesCount(int count);

    int getFriendsCount();

    void setFriendsCount(int count);

    int getKeyboardHeight();

    void setKeyboardHeight(int heightPx);

    int getOrientation();

    long getLastFetchStickersTime();

    void setLastFetchStickersTime(long timestamp);

    int getCurrentEmojiTab();

    void setCurrentEmojiTab(int tabNum);

    void setMarkMessagesAsRead(boolean markMessagesAsRead);

    boolean markMessagesAsRead();

    /**
     * Should set, if user wants to display unread messages as unread.
     * Or just display them like regular messages. This flag is only for incoming messages
     */
    void setDisplayUnreadMessagesAsUnread(boolean accessible);

    /**
     * Should return true or false, depending on if user wants to display unread messages as
     * unread.
     * Or just display them like regular messages. This flag is only for incoming messages
     */
    boolean isDisplayingUnreadMessagesAsUnread();


    @WorkerThread
    void addConversationToSyncUnreadMessages(long conversationId, long toTime);

    @WorkerThread
    void removeConversationToSyncUnreadMessages();

    @WorkerThread
    Set<MarkConversationReadTask> getConversationsToSyncUnreadMessages();
}
