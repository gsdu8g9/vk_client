package com.nethergrim.vk;

import com.vk.sdk.VKScope;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class Constants {

    public static final String VK_APP_ID = "4579411";
    public static final String[] PERMISSIONS = new String[] {
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.MESSAGES,
            VKScope.NOTIFICATIONS
    };

    public static float mDensity;

    public interface Requests {

        String MESSAGES_GET_DIALOGS = "messages.getDialogs";
        String GET_USERS = "users.get";
        String ACCOUNT_GET_PROFILE_INFO = "account.getProfileInfo";
    }

    public static float getDensity() {
        return mDensity;
    }
}
