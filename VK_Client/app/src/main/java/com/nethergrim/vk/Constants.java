package com.nethergrim.vk;

import com.vk.sdk.VKScope;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class Constants {


    public static final String VK_APP_ID = "4579411";
    public static final String GCM_SENDER_ID = "793065187";

    public static final String[] PERMISSIONS = new String[] {
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.MESSAGES,
            VKScope.NOTIFICATIONS, VKScope.NOTIFY
    };

    public static float mDensity;

    public interface Requests {

        String MESSAGES_GET_DIALOGS = "messages.getDialogs";
        String GET_USERS = "users.get";
        String ACCOUNT_GET_PROFILE_INFO = "account.getProfileInfo";
        String ACCOUNT_REGISTER_DEVICE = "account.registerDevice";
        String ACCOUNT_UNREGISTER_DEVICE = "account.unregisterDevice";
        String FRIENDS_GET = "friends.get";
    }

    public static float getDensity() {
        return mDensity;
    }
}
