package com.nethergrim.vk.utils;

import com.nethergrim.vk.models.User;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public interface UserProvider {


    User getUser(long userId);

    User getUser(String userId);

    User getMyUser();
}
