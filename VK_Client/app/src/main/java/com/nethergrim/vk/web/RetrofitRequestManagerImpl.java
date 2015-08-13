package com.nethergrim.vk.web;

import android.os.Build;
import android.support.annotation.Nullable;

import com.kisstools.utils.StringUtil;
import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfFriendIds;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserUtils;
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

        if (offset > 0) {
            params.put("offset", String.valueOf(offset));
        }
        if (limit != 0) {
            params.put("count", String.valueOf(limit));
        }
        if (onlyUnread) {
            params.put("unread", "1");
        }
        if (previewLenght > 0) {
            params.put("preview_length", String.valueOf(previewLenght));
        }

        return mRetrofitInterface.getConversations(params);
    }

    @Override
    @Nullable
    public ListOfUsers getUsers(List<Long> ids) {
        Map<String, String> params = getDefaultParamsMap();
        if (ids != null) {
            if (ids.size() > 1000) {
                throw new IllegalArgumentException("you want to fetch too much users. Max is 1000");
            }

            StringBuilder sb = new StringBuilder();
            for (Long id : ids) {
                sb.append(id);
                sb.append(", ");
            }
            String idsValues = StringUtil.cutText(sb.toString(), sb.toString().length() - 2);
            params.put("user_ids", idsValues);
        }

        StringBuilder sb = new StringBuilder();
        for (String field : UserUtils.getDefaultUserFields()) {
            sb.append(field);
            sb.append(", ");
        }
        String idsValues = StringUtil.cutText(sb.toString(), sb.toString().length() - 2);
        params.put("fields", idsValues);
        return mRetrofitInterface.getUsers(params);
    }

    @Override
    public User getCurrentUser() {
        Map<String, String> params = getDefaultParamsMap();
        params.put("fields", UserUtils.getDefaultUserFieldsAsString());
        return mRetrofitInterface.getCurrentUser(params);
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
        return mRetrofitInterface.getFriends(params);
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

        if (offset >= 0) {
            params.put("offset", String.valueOf(offset));
        }
        if (count > 0 && count <= 200) {
            params.put("count", String.valueOf(count));
        }

        if (userId > 0) {
            params.put("user_id", String.valueOf(userId));
        }
        if (chatId > 0) {
            params.put("chat_id", String.valueOf(chatId));
        }
        if (startMessageId > 0) {
            params.put("start_message_id", String.valueOf(startMessageId));
        } else if (reversedSorting) {
            params.put("rev", "1");
        }

        return mRetrofitInterface.getMessagesHistory(params);
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
