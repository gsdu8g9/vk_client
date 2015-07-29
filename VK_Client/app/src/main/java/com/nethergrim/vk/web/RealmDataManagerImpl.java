package com.nethergrim.vk.web;

import android.support.annotation.Nullable;
import android.util.Log;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.event.ConversationsUpdatedEvent;
import com.nethergrim.vk.event.UsersUpdatedEvent;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfFriendIds;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.User;
import com.squareup.otto.Bus;
import com.vk.sdk.api.VKError;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * @author andrej on 24.07.15.
 */
public class RealmDataManagerImpl implements DataManager {

    @Inject
    WebRequestManager mWebRequestManager;

    @Inject
    Bus mBus;

    @Inject
    Prefs mPrefs;


    public RealmDataManagerImpl() {
        MyApplication.getInstance().getMainComponent().inject(RealmDataManagerImpl.this);
    }

    @Override
    public void manageConversationsAndUsers(final int limit,
            final int offset,
            final boolean onlyUnread) {
        mWebRequestManager.getConversations(limit, offset, onlyUnread, 0,
                new WebCallback<ConversationsList>() {
                    @Override
                    public void onResponseSucceed(ConversationsList response) {
                        if (response != null) {
                            Realm realm = getRealm();
                            realm.beginTransaction();
                            mPrefs.setUnreadMessagesCount(response.getUnreadCount());
                            realm.copyToRealmOrUpdate(response.getResults());
                            realm.commitTransaction();
                            realm = null;
                            mBus.post(new ConversationsUpdatedEvent());
                            mWebRequestManager.getUsersForConversations(response,
                                    new WebCallback<ListOfUsers>() {
                                        @Override
                                        public void onResponseSucceed(ListOfUsers
                                                response) {
                                            Realm realm1 = getRealm();
                                            realm1.beginTransaction();
                                            realm1.copyToRealmOrUpdate(
                                                    response.getResponse());
                                            realm1.commitTransaction();
                                            realm1 = null;
                                            mBus.post(new ConversationsUpdatedEvent());
                                        }

                                        @Override
                                        public void onResponseFailed(VKError e) {
                                            Log.e("TAG", "Error in manageConversations: \n"
                                                    + e.toString());
                                            // TODO handle
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onResponseFailed(VKError e) {

                    }
                });

    }

    @Override
    public void manageUsers(final List<Long> userIds) {
        manageUsers(userIds, null);
    }

    @Override
    public void manageUsers(List<Long> userIds, final WebCallback<ListOfUsers> callback) {
        mWebRequestManager.getUsers(userIds, new WebCallback<ListOfUsers>() {
            @Override
            public void onResponseSucceed(ListOfUsers response) {
                Realm realm = getRealm();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.getResponse());
                realm.commitTransaction();
                realm = null;
                mBus.post(new UsersUpdatedEvent());
                if (callback != null) {
                    callback.onResponseSucceed(response);
                }
            }

            @Override
            public void onResponseFailed(VKError e) {
                Log.e("TAG", "error in ManageUsers: \n" + e.toString());
                if (callback != null) {
                    callback.onResponseFailed(e);
                }
            }
        });
    }

    @Override
    public void manageFriends(@Nullable final WebCallback<ListOfUsers> callback) {
        mWebRequestManager.getFriendsList(mPrefs.getCurrentUserId(),
                new WebCallback<ListOfFriendIds>() {
                    @Override
                    public void onResponseSucceed(ListOfFriendIds response) {
                        manageUsers(response.getIds(), callback);
                    }

                    @Override
                    public void onResponseFailed(VKError e) {
                        Log.e("TAG", "error: " + e.toString());
                    }
                });
    }

    @Override
    public void fetchMyUser() {
        mWebRequestManager.getCurrentUser(new WebCallback<User>() {
            @Override
            public void onResponseSucceed(User response) {
                mPrefs.setCurrentUserId(response.getId());
                Realm realm = getRealm();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response);
                realm.commitTransaction();
                realm = null;
                mBus.post(new UsersUpdatedEvent());
            }

            @Override
            public void onResponseFailed(VKError e) {
                Log.e("TAG", "error in fetchMyUser: \n" + e.toString());
            }
        });
    }

    private Realm getRealm() {
        Realm realm = Realm.getDefaultInstance();
        realm.setAutoRefresh(true);
        return realm;
    }
}
