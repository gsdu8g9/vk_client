package com.nethergrim.vk.web;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.data.PersistingManager;
import com.nethergrim.vk.event.ConversationUpdatedEvent;
import com.nethergrim.vk.event.ConversationsUpdatedEvent;
import com.nethergrim.vk.event.FriendsUpdatedEvent;
import com.nethergrim.vk.event.MyUserUpdatedEvent;
import com.nethergrim.vk.event.UsersUpdatedEvent;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.IntegerResponse;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.StartupResponse;
import com.nethergrim.vk.models.StickerDbItem;
import com.nethergrim.vk.models.StockItemsResponse;
import com.nethergrim.vk.models.WebResponse;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Class that will handle results of every web request. Should be used to map, persist data, and
 * notify UI thread to be updated.
 * <p>
 * This should be the only way to handle web requests results.
 * <p>
 * By default every method of current class should return {@link rx.Observable} as result.
 *
 * @author andrej on 30.08.15 (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class DataManagerImpl implements DataManager {


    private static final String TAG = DataManager.class.getSimpleName();

    @Inject
    Prefs mPrefs;


    @Inject
    WebRequestManager mWebRequestManager;

    @Inject
    PersistingManager mPersistingManager;

    @Inject
    Bus mBus;

    @Inject
    ImageLoader mImageLoader;

    public DataManagerImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public Observable<StartupResponse> launchStartupTasksAndPersistToDb() {

        return Observable.just(Boolean.TRUE)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(aBoolean -> {
                    // prepare parameters
                    String token = mPrefs.getGcmToken();
                    if (TextUtils.isEmpty(token)) {
                        InstanceID instanceID = InstanceID.getInstance(
                                MyApplication.getInstance().getApplicationContext());
                        try {
                            token = instanceID.getToken(Constants.GCM_SENDER_ID,
                                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mPrefs.setGcmToken(token);
                    }
                    return token;
                })
                .flatMap(mWebRequestManager::launchStartupTasks)
                .doOnNext(mPersistingManager::manage)
                .doOnNext(startupResponse -> mBus.post(new MyUserUpdatedEvent()))
                ;

    }

    @Override
    public Observable<ListOfFriends> fetchFriendsAndPersistToDb(int count, int offset) {
        return mWebRequestManager.getFriends(mPrefs.getCurrentUserId(), count, offset)
                .doOnNext(listOfFriends -> mPersistingManager.manage(listOfFriends, offset))
                .doOnNext(listOfFriends -> mBus.post(new FriendsUpdatedEvent(count)))
                ;
    }

    @Override
    public Observable<ListOfUsers> fetchUsersAndPersistToDB(List<Long> ids) {
        return mWebRequestManager.getUsers(ids).doOnNext(mPersistingManager::manage)
                .doOnNext(listOfUsers -> mBus.post(new UsersUpdatedEvent()))
                ;
    }

    @Override
    public Observable<ConversationsUserObject> fetchConversationsUserAndPersist(int limit,
            int offset,
            boolean unreadOnly) {
        return mWebRequestManager.getConversationsAndUsers(limit, offset, unreadOnly)
                .doOnNext(conversationsUserObject -> mPersistingManager.manage(
                        conversationsUserObject, offset == 0))
                .doOnNext(conversationsUserObject -> {
                    mBus.post(new ConversationsUpdatedEvent());
                    mBus.post(new UsersUpdatedEvent());
                });
    }

    @Override
    public Observable<ListOfMessages> fetchMessagesHistory(int count,
            int offset,
            String userId,
            long chatId) {
        return mWebRequestManager
                .getChatHistory(offset, count, userId, chatId)
                .doOnNext(listOfMessages -> {
                    Log.d(TAG, "fetched messages: \n" + "count: " + count + " offset: " + offset
                            + " userId: " + userId + " chatId: " + chatId);
                    mPersistingManager.manage(listOfMessages);
                    ConversationUpdatedEvent conversationUpdatedEvent
                            = new ConversationUpdatedEvent(listOfMessages, userId, chatId);
                    mBus.post(conversationUpdatedEvent);
                })
                ;
    }

    @Override
    public Observable<IntegerResponse> deleteConversation(long userId, long chatId) {
        return mWebRequestManager.deleteConversation(userId, chatId)
                .doOnNext(integerResponse -> {
                    // delete conversation from local database
                    mPersistingManager.deleteConversation(userId, chatId);
                    mBus.post(new ConversationsUpdatedEvent());
                })
                ;
    }

    @Override
    public Observable<List<StickerDbItem>> getStickerItems() {
        return mWebRequestManager.getStickerStockItems()
                .map(StockItemsResponse::getStockItems)
                .map(stockItems -> {
                    if (stockItems == null || stockItems.getItems() == null) {
                        return null;
                    }
                    List<StickerDbItem> result = new ArrayList<>(
                            stockItems.getItems().size());
                    for (int i = 0, size = stockItems.getItems().size(); i < size; i++) {
                        result.add(StickerDbItem.MAPPER.call(stockItems.getItems().get(i)));
                        String url = result.get(i).getPhoto();
                        mImageLoader.cacheToMemory(url);
                    }
                    return result;
                })
                .doOnNext(stickerDbItems -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(stickerDbItems);
                    realm.commitTransaction();
                    realm.close();

                });
    }

    @Override
    public Observable<WebResponse> markMessagesAsRead(long conversationsId, long toTime) {
        // add messages to sync in preference
        // then sync messages read state

        // TODO: 06.12.15
        return syncMessagesReadState();
    }

    @Override
    public Observable<WebResponse> syncMessagesReadState() {
        // TODO: 06.12.15
        return null;
    }
}
