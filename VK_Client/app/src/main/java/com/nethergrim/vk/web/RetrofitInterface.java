package com.nethergrim.vk.web;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.StartupResponse;

import java.util.Map;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * @author andrej on 12.08.15.
 */
public interface RetrofitInterface {

    @GET(Constants.Requests.GET_USERS)
    ListOfUsers getUsers(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.GET_USERS)
    Observable<ListOfUsers> getUsersObservable(@QueryMap Map<String, String> options);

    @POST(Constants.Requests.EXECUTE_POST_STARTUP)
    StartupResponse launchStartupTasks(@QueryMap Map<String, String> options);

    @POST(Constants.Requests.ACCOUNT_UNREGISTER_DEVICE)
    Response unregisterFromPushes(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.EXECUTE_GET_FRIENDS)
    ListOfFriends getFriends(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.MESSAGES_GET_HISTORY)
    ListOfMessages getMessagesHistory(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.EXECUTE_GET_CONVERSATIONS_AND_USERS)
    ConversationsUserObject getConversationsAndUsers(@QueryMap Map<String, String> options);
}
