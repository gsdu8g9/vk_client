package com.nethergrim.vk.web;

import android.os.Looper;
import android.os.Process;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.LongToLongModel;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.data.Store;
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
import com.nethergrim.vk.models.PendingMessage;
import com.nethergrim.vk.models.StartupResponse;
import com.nethergrim.vk.models.StickerDbItem;
import com.nethergrim.vk.models.StockItemsResponse;
import com.nethergrim.vk.models.WebResponse;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Class that will handle results of every web request. Should be used to map, persist data, and
 * notify UI thread to be updated.
 * <p>
 * This should be the only way to handle web requests results.
 * <p>
 * By default every method of current class should return {@link rx.Observable} as result.
 *
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 30.08.15 (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class DataManagerImpl implements DataManager {


    private static final String TAG = DataManager.class.getSimpleName();

    @Inject
    Prefs mPrefs;

    @Inject
    WebRequestManager mWebRequestManager;

    @Inject
    Store mPersistingManager;

    @Inject
    Bus mBus;

    @Inject
    ImageLoader mImageLoader;

    @Inject
    Store mStore;

    private Scheduler singleThreadScheduler = Schedulers.from(Executors.newSingleThreadExecutor());

    public DataManagerImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
        singleThreadScheduler.createWorker().schedule(() -> {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Thread.currentThread().setName("Messaging thread");
        });
    }

    @NonNull
    @Override
    public Observable<StartupResponse> launchStartupTasksAndPersistToDb() {

        return Observable.just(Boolean.TRUE)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(aBoolean -> {
                    // prepare parameters
                    String token = mPrefs.getGcmToken();
                    if (TextUtils.isEmpty(token)) {

                        try {
                            InstanceID instanceID = InstanceID.getInstance(MyApplication.getInstance().getApplicationContext());
                            token = instanceID.getToken(Constants.GCM_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            Log.e(TAG, "launchStartupTasksAndPersistToDb: ", e);
                        }
                        mPrefs.setGcmToken(token);
                    }
                    return token;
                })
                .flatMap(mWebRequestManager::launchStartupTasks)
                .doOnNext(mPersistingManager::persist)
                .doOnNext(startupResponse -> mBus.post(new MyUserUpdatedEvent()));
    }

    @NonNull
    @Override
    public Observable<ListOfFriends> fetchFriendsAndPersistToDb(int count, int offset) {
        return mWebRequestManager.getFriends(mPrefs.getCurrentUserId(), count, offset)
                .doOnNext(listOfFriends -> mPersistingManager.persist(listOfFriends, offset))
                .doOnNext(listOfFriends -> mBus.post(new FriendsUpdatedEvent(count)))
                ;
    }

    @NonNull
    @Override
    public Observable<ListOfUsers> fetchUsersAndPersistToDB(List<Long> ids) {
        return mWebRequestManager.getUsers(ids).doOnNext(mPersistingManager::persist)
                .doOnNext(listOfUsers -> mBus.post(new UsersUpdatedEvent()))
                ;
    }

    @NonNull
    @Override
    public Observable<ConversationsUserObject> fetchConversationsUserAndPersist(int limit,
                                                                                int offset,
                                                                                boolean unreadOnly) {
        return mWebRequestManager.getConversationsAndUsers(limit, offset, unreadOnly)
                .doOnNext(conversationsUserObject -> mPersistingManager.persist(
                        conversationsUserObject, offset == 0))
                .doOnNext(conversationsUserObject -> {
                    mBus.post(new ConversationsUpdatedEvent());
                    mBus.post(new UsersUpdatedEvent());
                });
    }

    @NonNull
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
                    mPersistingManager.persist(listOfMessages);
                    ConversationUpdatedEvent conversationUpdatedEvent
                            = new ConversationUpdatedEvent(listOfMessages, userId, chatId);
                    mBus.post(conversationUpdatedEvent);
                })
                ;
    }

    @NonNull
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

    @NonNull
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
                        mImageLoader.preCache(url);
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

    @NonNull
    @Override
    public Observable<WebResponse> markMessagesAsRead(long conversationId, long lastMessageId) {
        // add messages to sync in preference
        // then sync messages read state
        return Observable.fromCallable(() -> {
            mPrefs.addConversationToSyncUnreadMessages(conversationId, lastMessageId);
            return Boolean.TRUE;
        }).flatMap(new Func1<Boolean, Observable<WebResponse>>() {
            @Override
            public Observable<WebResponse> call(Boolean aBoolean) {
                return syncMessagesReadState();
            }
        });
    }

    @NonNull
    @Override
    public Observable<WebResponse> syncMessagesReadState() {
        if (!mPrefs.markMessagesAsRead() && mPrefs.isDisplayingUnreadMessagesAsUnread()) {
            // fake
            return ReactiveNetwork.observeInternetConnectivity()
                    .subscribeOn(Schedulers.io())
                    .first()
                    .filter(aBoolean -> aBoolean)
                    .map(aBoolean -> mPrefs.getConversationsToSyncUnreadMessages())
                    .filter(a -> !a.isEmpty())
                    .flatMap(Observable::from, 4)
                    .flatMap(new Func1<LongToLongModel, Observable<WebResponse>>() {
                        @Override
                        public Observable<WebResponse> call(LongToLongModel longToLongModel) {
                            long conversationId = longToLongModel.getL1();
                            long lastMessageId = longToLongModel.getL2();
                            mStore.markMessagesAsRead(conversationId, lastMessageId);
                            return Observable.empty();
                        }
                    }, 4)
                    .doOnCompleted(() -> mPrefs.removeConversationToSyncUnreadMessages());


        }
        //real
        return ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .first()
                .filter(aBoolean -> aBoolean)
                .map(aBoolean -> mPrefs.getConversationsToSyncUnreadMessages())
                .filter(a -> !a.isEmpty())
                .flatMap(Observable::from, 4)
                .flatMap(new Func1<LongToLongModel, Observable<WebResponse>>() {
                    @Override
                    public Observable<WebResponse> call(LongToLongModel longToLongModel) {
                        long conversationId = longToLongModel.getL1();
                        long lastMessageId = longToLongModel.getL2();
                        return Observable.fromCallable(() -> {
                            WebResponse webResponse = mWebRequestManager.markMessagesAsRead(conversationId, lastMessageId).first().toBlocking().first();
                            if (webResponse.ok()) {
                                mStore.markMessagesAsRead(conversationId, lastMessageId);
                            }
                            return webResponse;
                        });
                    }
                }, 4)
                .doOnCompleted(() -> mPrefs.removeConversationToSyncUnreadMessages());

    }

    @NonNull
    @Override
    public Observable<WebResponse> sendMessageOrSchedule(long peerId, @NonNull PendingMessage pendingMessage) {
        return ReactiveNetwork.observeInternetConnectivity()
                .first()
                .subscribeOn(singleThreadScheduler)
                .observeOn(singleThreadScheduler)
                .flatMap(new Func1<Boolean, Observable<WebResponse>>() {
                    @Override
                    public Observable<WebResponse> call(Boolean aBoolean) {
                        return aBoolean ? sendMessage(peerId, pendingMessage) : scheduleMessageToSendLater(peerId, pendingMessage);
                    }
                });
    }

    @NonNull
    @Override
    public Observable<WebResponse> syncPendingMessages() {
        return Observable.from(mStore.getUnsentMessages())
                .subscribeOn(singleThreadScheduler)
                .observeOn(singleThreadScheduler)
                .flatMap(pendingMessage -> mWebRequestManager.sendMessage(pendingMessage.getPeerId(), pendingMessage))
                .observeOn(singleThreadScheduler);
    }

    private Observable<WebResponse> sendMessage(long peerId, @NonNull PendingMessage pendingMessage) {
        failIfOnMainThread();
        mStore.savePendingMessage(peerId, pendingMessage);
        return syncPendingMessages();
    }


    private Observable<WebResponse> scheduleMessageToSendLater(long peerId, @NonNull PendingMessage pendingMessage) {
        failIfOnMainThread();
        mStore.savePendingMessage(peerId, pendingMessage);
        return Observable.empty();
    }

    private void failIfOnMainThread() {
        if (mainThread()) {
            throw new IllegalStateException("MAIN THREAD");
        }
    }

    private boolean mainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
