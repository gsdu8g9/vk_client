package com.nethergrim.vk.web;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.IntegerResponse;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessagesResponse;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.StartupResponse;
import com.nethergrim.vk.models.StockItemsResponse;
import com.nethergrim.vk.models.WebResponse;

import java.util.Map;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 12.08.15.
 */
public interface RetrofitInterface {

    @GET(Constants.Requests.GET_USERS)
    Observable<ListOfUsers> getUsers(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.GET_USERS)
    Observable<ListOfUsers> getUsersObservable(@QueryMap Map<String, String> options);

    @POST(Constants.Requests.EXECUTE_POST_STARTUP)
    Observable<StartupResponse> launchStartupTasks(@QueryMap Map<String, String> options);

    @POST(Constants.Requests.ACCOUNT_UNREGISTER_DEVICE)
    Observable<Response> unregisterFromPushes(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.EXECUTE_GET_FRIENDS)
    Observable<ListOfFriends> getFriends(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.MESSAGES_GET_HISTORY)
    Observable<ListOfMessagesResponse> getMessagesHistory(@QueryMap Map<String, String> options);

    @GET(Constants.Requests.EXECUTE_GET_CONVERSATIONS_AND_USERS)
    Observable<ConversationsUserObject> getConversationsAndUsers(
            @QueryMap Map<String, String> options);

    @POST(Constants.Requests.EXECUTE_DELETE_CONVERSATION)
    Observable<IntegerResponse> deleteConversation(@QueryMap Map<String, String> options);

    @POST(Constants.Requests.GET_STICKER_PURCHASES)
    Observable<StockItemsResponse> getStickers(@QueryMap Map<String, String> options);

    @POST(Constants.Requests.MESSAGES_MARK_AS_READ)
    Observable<WebResponse> markMessagesAsRead(@QueryMap Map<String, String> options);
}
