package com.nethergrim.vk.web;

import android.text.TextUtils;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfFriendIds;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

/**
 * @author andrej on 12.08.15.
 */
public class RetrofitRequestManagerImpl implements WebRequestManager {

    RetrofitInterface mRetrofitInterface;
    @Inject
    Prefs mPrefs;
    private Map<String, String> mDefaultParamsMap;

    public RetrofitRequestManagerImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASIC_API_URL)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setConverter(new JacksonConverter())
                .build();
        mRetrofitInterface = restAdapter.create(RetrofitInterface.class);
        initDefaultParamsMap();
    }

    @Override
    public void getConversations(int limit,
            int offset,
            boolean onlyUnread,
            int previewLength,
            WebCallback<ConversationsList> callback) {

    }

    @Override
    public void getUsers(List<Long> ids, WebCallback<ListOfUsers> callback) {

    }


    @Override
    public void getCurrentUser(WebCallback<User> callback) {

    }

    @Override
    public void registerToPushNotifications(String token) {

    }

    @Override
    public void unregisterFromPushNotifications() {

    }

    @Override
    public void getFriendsList(long userId, WebCallback<ListOfFriendIds> callback) {

    }

    @Override
    public void registerOnline() {

    }

    @Override
    public void getChatHistory(int offset,
            int count,
            long userId,
            long chatId,
            long startMessageId,
            boolean reversedSorting,
            WebCallback<ListOfMessages> callback) {

    }

    private void initDefaultParamsMap() {
        if (mDefaultParamsMap == null) {
            mDefaultParamsMap = new HashMap<>();
        } else {
            mDefaultParamsMap.clear();
        }
        mDefaultParamsMap.put("v", Constants.API_VERSION);
        mDefaultParamsMap.put("https", "1");
        mDefaultParamsMap.put("lang", Locale.getDefault().getDisplayCountry());
        mDefaultParamsMap.put("access_token", mPrefs.getToken());
    }

    /**
     * To be called before every web request that requires token
     */
    private boolean checkAccessToken() {
        mDefaultParamsMap.put("access_token",
                mPrefs.getToken()); // to update token for web requests from prefs
        return !TextUtils.isEmpty(mPrefs.getToken());
    }
}
