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
}
