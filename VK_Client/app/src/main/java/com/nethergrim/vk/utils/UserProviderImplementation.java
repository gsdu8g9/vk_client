package com.nethergrim.vk.utils;

import com.nethergrim.vk.models.User;

import io.realm.Realm;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class UserProviderImplementation implements UserProvider {

    private Realm mRealm;

    public UserProviderImplementation() {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public User getUser(long userId) {
        return mRealm.where(User.class).equalTo("id", userId).findFirst();
    }
}
