package com.nethergrim.vk;

import com.vk.sdk.VKScope;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class Constants {


    public static final String VK_APP_ID = "4579411";
    public static final String GCM_SENDER_ID = "793065187";
    public static final int ANIMATION_DURATION = 250;
    public static final String PACKAGE_NAME = "com.nethergrim.vk";
    public static final String EXTRA_CONVERSATION_ID = Constants.PACKAGE_NAME
            + ".CONVERSATION_ID";
    public static final String EXTRA_GROUP_CONVERSATION = Constants.PACKAGE_NAME
            + ".GROUP_CONVERSATION";
    public static final String BASIC_API_URL = "https://api.vk.com/method/";
    public static final String API_VERSION = "5.37";


    public static final String[] PERMISSIONS = new String[] {
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.MESSAGES,
            VKScope.NOTIFICATIONS, VKScope.NOTIFY
    };

    public static float mDensity;

    public interface Requests {

        String MESSAGES_GET_DIALOGS = "/messages.getDialogs";
        String MESSAGES_GET_HISTORY = "/messages.getHistory";
        String GET_USERS = "/users.get";
        String ACCOUNT_REGISTER_DEVICE = "/account.registerDevice";
        String ACCOUNT_UNREGISTER_DEVICE = "/account.unregisterDevice";
        String FRIENDS_GET = "/friends.get";
        String ACCOUNT_SETONLINE = "/account.setOnline";
        String EXECUTE_GET_CONVERSATIONS_AND_USERS = "/execute.getConversationsAndUsers";
        String EXECUTE_GET_FRIENDS = "/execute.getFriends";
        String EXECUTE_POST_STARTUP = "/execute.startup";
        String EXECUTE_DELETE_CONVERSATION = "/execute.deleteConversation";
    }

    public static float getDensity() {
        return mDensity;
    }
}
