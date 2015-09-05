package com.nethergrim.vk.web;

import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kisstools.utils.StringUtil;
import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.StartupResponse;
import com.nethergrim.vk.models.WebResponse;
import com.nethergrim.vk.utils.RetryWithDelay;
import com.nethergrim.vk.utils.UserUtils;
import com.nethergrim.vk.utils.Utils;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author andrej on 12.08.15.
 */
public class WebRequestManagerImpl implements WebRequestManager {

    public static final String TAG = WebRequestManagerImpl.class.getSimpleName();
    public static final int MAX_RETRY_COUNT = 10;
    public static final int MIN_RETRY_DELAY_MS = 800;
    RetrofitInterface mRetrofitInterface;
    @Inject
    Prefs mPrefs;
    private Action1<WebResponse> mDefaultResponseChecker = webResponse -> {
        if (!webResponse.ok()) {
            throw new VkApiError(webResponse.getError());
        }
    };
    private RetryWithDelay mRetryCall = new RetryWithDelay(MAX_RETRY_COUNT, MIN_RETRY_DELAY_MS);
    private Action1<Throwable> mErrorCall = (e -> {
        if (e instanceof UnknownHostException) {
            // ignore, just no internet connection
        } else {
            Log.e("WebError", e.toString() + " " + e.getMessage());
            // TODO: 05.09.15 add analytics handling here
        }
    }
    );

    public WebRequestManagerImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASIC_API_URL)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setConverter(new JacksonConverter())
                .setClient(new OkClient())
                .build();
        mRetrofitInterface = restAdapter.create(RetrofitInterface.class);
    }

    public Scheduler getDefaultScheduler() {
        return Schedulers.io();
    }

    @Override
    @Nullable
    public Observable<ListOfUsers> getUsers(List<Long> ids) {
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
        return mRetrofitInterface.getUsers(params)
                .doOnNext(mDefaultResponseChecker)
                .retryWhen(mRetryCall)
                .doOnError(mErrorCall)
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler());
    }

    @Override
    public Observable<Response> unregisterFromPushNotifications() {
        Map<String, String> params = getDefaultParamsMap();
        params.put("device_id", Utils.generateAndroidId());
        return mRetrofitInterface.unregisterFromPushes(params)
                .retryWhen(mRetryCall)
                .doOnError(mErrorCall)
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler());
    }

    @Override
    public Observable<ListOfFriends> getFriends(long userId, int count, int offset) {
        Map<String, String> params = getDefaultParamsMap();
        params.put("userId", String.valueOf(userId));
        params.put("count", String.valueOf(count));
        params.put("offset", String.valueOf(offset));
        params.put("fields", UserUtils.getDefaultUserFieldsAsString());
        return mRetrofitInterface.getFriends(params)
                .doOnNext(mDefaultResponseChecker)
                .retryWhen(mRetryCall)
                .doOnError(mErrorCall)
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler());
    }

    @Override
    public Observable<StartupResponse> launchStartupTasks(String gcmToken) {
        Map<String, String> params = getDefaultParamsMap();
        params.put("gcmToken", gcmToken);
        params.put("fields", UserUtils.getDefaultUserFieldsAsString());
        params.put("deviceId", Utils.generateAndroidId());
        params.put("systemVersion", String.valueOf(Build.VERSION.SDK_INT));
        // FIXME fix settings
        params.put("settings", "{\"msg\":\"on\", \"chat\":[\"no_sound\",\"no_text\"], "
                + "\"friend\":\"on\", \"reply\":\"on\", \"mention\":\"fr_of_fr\"} ");

        return mRetrofitInterface.launchStartupTasks(params)
                .doOnNext(mDefaultResponseChecker)
                .retryWhen(mRetryCall)
                .doOnError(mErrorCall)
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler());
    }

    @Override
    public Observable<ConversationsUserObject> getConversationsAndUsers(int limit,
            int offset,
            boolean unread) {
        Map<String, String> params = getDefaultParamsMap();

        if (offset > 0) {
            params.put("offset", String.valueOf(offset));
        }
        if (limit != 0) {
            params.put("count", String.valueOf(limit));
        }
        if (unread) {
            params.put("unread", "1");
        }
        return mRetrofitInterface.getConversationsAndUsers(params)
                .doOnNext(mDefaultResponseChecker)
                .retryWhen(mRetryCall)
                .doOnError(mErrorCall)
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler());
    }

    @Override
    public Observable<ListOfMessages> getChatHistory(int offset,
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

        return mRetrofitInterface.getMessagesHistory(params)
                .doOnNext(mDefaultResponseChecker)
                .retryWhen(mRetryCall)
                .doOnError(mErrorCall)
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler());
    }

    private Map<String, String> getDefaultParamsMap() {
        // TODO: 04.09.15 object pools?
        Map<String, String> defaultParamsMap = new HashMap<>();
        defaultParamsMap.put("v", Constants.API_VERSION);
        defaultParamsMap.put("https", "1");
        defaultParamsMap.put("lang", Locale.getDefault().getDisplayCountry());
        defaultParamsMap.put("access_token", mPrefs.getToken());
        return defaultParamsMap;
    }

}
