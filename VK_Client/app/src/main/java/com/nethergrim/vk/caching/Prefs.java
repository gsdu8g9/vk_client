package com.nethergrim.vk.caching;

/**
 * @author andreydrobyazko on 3/20/15.
 */
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
}
