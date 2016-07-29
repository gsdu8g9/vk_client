package com.nethergrim.vk.web;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.data.Store;
import com.nethergrim.vk.event.ErrorDuringSendingMessageEvent;
import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.IntegerResponse;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfMessagesResponse;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.PendingMessage;
import com.nethergrim.vk.models.StartupResponse;
import com.nethergrim.vk.models.StockItemsResponse;
import com.nethergrim.vk.models.WebResponse;
import com.nethergrim.vk.utils.RetryWithDelay;
import com.nethergrim.vk.utils.UserUtils;
import com.nethergrim.vk.utils.Utils;
import com.squareup.otto.Bus;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 12.08.15.
 */
public class WebRequestManagerImpl implements WebRequestManager {

    public static final String TAG = WebRequestManagerImpl.class.getSimpleName();
    public static final int MAX_RETRY_COUNT = 10;
    public static final int MIN_RETRY_DELAY_MS = 800;
    private RetrofitInterface mRetrofitInterface;
    @Inject
    Prefs mPrefs;

    @Inject
    Store store;

    @Inject
    Bus bus;
    private Action1<WebResponse> mDefaultResponseChecker = webResponse -> {
        if (!webResponse.ok()) {
            throw new VkApiError(webResponse.getError());
        }
    };


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

    private Scheduler getDefaultScheduler() {
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
            String s = sb.toString();
            String idsValues = s.substring(0, s.length() - 2);
            params.put("user_ids", idsValues);
        }

        StringBuilder sb = new StringBuilder();
        for (String field : UserUtils.getDefaultUserFields()) {
            sb.append(field);
            sb.append(", ");
        }
        String s = sb.toString();
        String idsValues = s.substring(0, s.length() - 2);
        params.put("fields", idsValues);
        return mRetrofitInterface.getUsers(params)
                .doOnNext(mDefaultResponseChecker)
                .retryWhen(RetryWithDelay.getInstance())
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler());
    }

    @Override
    public Observable<Response> unregisterFromPushNotifications() {
        Map<String, String> params = getDefaultParamsMap();
        params.put("device_id", Utils.generateAndroidId());
        return mRetrofitInterface.unregisterFromPushes(params)
                .retryWhen(RetryWithDelay.getInstance())
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
                .retryWhen(RetryWithDelay.getInstance())
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
                .retryWhen(RetryWithDelay.getInstance())
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
                .retryWhen(RetryWithDelay.getInstance())
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler());
    }

    @Override
    public Observable<ListOfMessages> getChatHistory(int offset,
                                                     int count,
                                                     String userId,
                                                     long chatId
    ) {
        Map<String, String> params = getDefaultParamsMap();

        if (offset >= 0) {
            params.put("offset", String.valueOf(offset));
        }
        if (count > 0 && count <= 200) {
            params.put("count", String.valueOf(count));
        }

        if (!TextUtils.isEmpty(userId)) {
            params.put("peer_id", userId);
        } else {
            params.put("peer_id", String.valueOf(chatId + 2000000000));
        }
        return mRetrofitInterface.getMessagesHistory(params)
                .doOnNext(mDefaultResponseChecker)
                .retryWhen(RetryWithDelay.getInstance())
                .map(ListOfMessagesResponse::getListOfMessages)
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler());
    }

    @Override
    public Observable<IntegerResponse> deleteConversation(long userId, long chatId) {
        Map<String, String> params = getDefaultParamsMap();

        if (userId > 0) {
            params.put("userId", String.valueOf(userId));
        } else {
            params.put("userId", String.valueOf(0));
        }
        if (chatId > 0) {
            params.put("chatId", String.valueOf(chatId));
        }
        return mRetrofitInterface.deleteConversation(params)
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler())
                .doOnNext(mDefaultResponseChecker)
                .retryWhen(RetryWithDelay.getInstance());
    }

    @Override
    public Observable<StockItemsResponse> getStickerStockItems() {

        Map<String, String> params = getDefaultParamsMap();

        params.put("merchant", "google");
        params.put("type", "stickers");
        return mRetrofitInterface.getStickers(params)
                .observeOn(getDefaultScheduler())
                .subscribeOn(getDefaultScheduler())
                .doOnNext(mDefaultResponseChecker)
                .retryWhen(RetryWithDelay.getInstance());
    }

    /**
     * Should not fire request if marking messages as read is turned off.
     *
     * @param peerId         Id of user or conversation
     * @param startMessageId Id of last message that should be marked as read.
     * @return Observable of web request if marking messages as read is enabled.
     * Empty observable if it's not allowed.
     */
    @Override
    public Observable<WebResponse> markMessagesAsRead(long peerId, long startMessageId) {
        if (!mPrefs.markMessagesAsRead()) {
            return Observable.empty();
        }
        Map<String, String> params = getDefaultParamsMap();
        params.put("peer_id", String.valueOf(peerId));
        params.put("start_message_id", String.valueOf(startMessageId));
        return mRetrofitInterface.markMessagesAsRead(params)
                .subscribeOn(getDefaultScheduler())
                .doOnNext(mDefaultResponseChecker)
                .retryWhen(RetryWithDelay.getInstance());
    }

    @Override
    @DebugLog
    public Observable<WebResponse> sendMessage(long peerId, @NonNull PendingMessage pendingMessage) {
        Map<String, String> params = getDefaultParamsMap();
        params.put("peer_id", String.valueOf(peerId));
        params.put("random_id", String.valueOf(pendingMessage.getRandomId()));

        String message = pendingMessage.getText();
        if (message != null && message.length() > 0) {
            params.put("message", message);
        }

        String forwarded = pendingMessage.getForwardedMessageIds();
        if (forwarded != null && forwarded.length() > 0) {
            params.put("forward_messages", forwarded);
        }

        try {
            @SuppressWarnings("ConstantConditions") long stickerId = pendingMessage.getStickerId();
            if (stickerId > 0) {
                params.put("sticker_id", String.valueOf(stickerId));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return mRetrofitInterface.sendMessage(params)
                .retryWhen(new RetryWithDelay(10, 250))
                .doOnNext(webResponse -> {
                    if (webResponse.ok()) {
                        store.removePendingMessage(peerId, pendingMessage.getRandomId());
                    } else {
                        bus.post(new ErrorDuringSendingMessageEvent(webResponse));
                    }
                })
                .doOnNext(mDefaultResponseChecker);
    }

    private Map<String, String> getDefaultParamsMap() {
        // TODO: 04.09.15 object pools?
        Map<String, String> defaultParamsMap = new HashMap<>();
        defaultParamsMap.put("v", Constants.VK_API_VERSION);
        defaultParamsMap.put("https", "1");
        defaultParamsMap.put("lang", Locale.getDefault().getDisplayCountry());
        defaultParamsMap.put("access_token", mPrefs.getToken());
        return defaultParamsMap;
    }

}
