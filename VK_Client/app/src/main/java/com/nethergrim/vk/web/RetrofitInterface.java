package com.nethergrim.vk.web;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfFriendIds;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.User;

import java.util.Map;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * @author andrej on 12.08.15.
 */
public interface RetrofitInterface {

    @GET(Constants.Requests.MESSAGES_GET_DIALOGS)
    ConversationsList getConversations(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.GET_USERS)
    ListOfUsers getUsers(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.GET_USERS)
    User getCurrentUser(@QueryMap Map<String, String> options);

    @POST(Constants.Requests.ACCOUNT_REGISTER_DEVICE)
    Response registerToPushNotifications(@QueryMap Map<String, String> options);

    @POST(Constants.Requests.ACCOUNT_UNREGISTER_DEVICE)
    Response unregisterFromPushes(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.FRIENDS_GET)
    ListOfFriendIds getFriends(@QueryMap Map<String, String> options);

    @POST(Constants.Requests.ACCOUNT_SETONLINE)
    Response setOnline(@QueryMap Map<String, String> options);
}
