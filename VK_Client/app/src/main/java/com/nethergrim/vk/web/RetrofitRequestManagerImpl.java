package com.nethergrim.vk.web;

import android.os.Build;
import android.support.annotation.Nullable;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfFriendIds;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;

/**
 * @author andrej on 12.08.15.
 */
public class RetrofitRequestManagerImpl implements WebRequestManager {

    RetrofitInterface mRetrofitInterface;
    @Inject
    Prefs mPrefs;


    public RetrofitRequestManagerImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASIC_API_URL)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setConverter(new JacksonConverter())
                .build();
        mRetrofitInterface = restAdapter.create(RetrofitInterface.class);
    }


    @Override
    public ConversationsList getConversations(int limit,
            int offset,
            boolean onlyUnread,
            int previewLenght) {
        Map<String, String> params = getDefaultParamsMap();

        return null;
    }

    @Override
    @Nullable
    public ListOfUsers getUsers(List<Long> ids) {
        Map<String, String> params = getDefaultParamsMap();

        return null;
    }

    @Override
    public User getCurrentUser() {
        Map<String, String> params = getDefaultParamsMap();

        return null;
    }

    @Override
    public boolean registerToPushNotifications(String token) {
        Map<String, String> params = getDefaultParamsMap();
        params.put("token", token);
        params.put("device_model", "android");
        params.put("device_id", Utils.generateAndroidId());
        params.put("system_version", String.valueOf(Build.VERSION.SDK_INT));
        // TODO fix settings
        params.put("settings", "{\"msg\":\"on\", \"chat\":[\"no_sound\",\"no_text\"], "
                + "\"friend\":\"on\", \"reply\":\"on\", \"mention\":\"fr_of_fr\"} ");
        return isResponseSuccessful(mRetrofitInterface.registerToPushNotifications(params));
    }

    @Override
    public boolean unregisterFromPushNotifications() {
        Map<String, String> params = getDefaultParamsMap();
        params.put("device_id", Utils.generateAndroidId());
        return isResponseSuccessful(mRetrofitInterface.unregisterFromPushes(params));
    }


    @Override
    public ListOfFriendIds getFriendsList(long userId) {
        Map<String, String> params = getDefaultParamsMap();
        params.put("user_id", String.valueOf(userId));
        params.put("order", "random");
        ListOfFriendIds listOfFriendIds = mRetrofitInterface.getFriends(params);
        return listOfFriendIds;
    }

    @Override
    public boolean registerOnline() {
        Map<String, String> params = getDefaultParamsMap();
        params.put("voip", "0");
        return isResponseSuccessful(mRetrofitInterface.setOnline(params));
    }

    @Override
    public ListOfMessages getChatHistory(int offset,
            int count,
            long userId,
            long chatId,
            long startMessageId,
            boolean reversedSorting) {
        Map<String, String> params = getDefaultParamsMap();

        return null;
    }

    private boolean isResponseSuccessful(Response r) {
        return r.getStatus() == 200 || r.getStatus() == 201;
    }

    private Map<String, String> getDefaultParamsMap() {
        // TODO possible place to implement pools (for maps)
        Map<String, String> defaultParamsMap = new HashMap<>();
        defaultParamsMap.put("v", Constants.API_VERSION);
        defaultParamsMap.put("https", "1");
        defaultParamsMap.put("lang", Locale.getDefault().getDisplayCountry());
        defaultParamsMap.put("access_token", mPrefs.getToken());
        return defaultParamsMap;
    }

}
