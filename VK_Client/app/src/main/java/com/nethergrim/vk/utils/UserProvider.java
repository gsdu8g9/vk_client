package com.nethergrim.vk.utils;

import android.support.annotation.Nullable;

import com.nethergrim.vk.models.User;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public interface UserProvider {


    @Nullable
    User getUser(long userId);

    @Nullable
    User getUser(String userId);

    @Nullable
    User getMyUser();
}
