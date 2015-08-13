package com.nethergrim.vk.web;

import android.support.annotation.NonNull;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.squareup.otto.Bus;

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
    public void fetchConversationsAndUsers(final int limit,
            final int offset,
            final boolean onlyUnread) {

    }

    @Override
    public void fetchUsers(@NonNull final List<Long> userIds) {
    }

    @Override
    public void fetchMyFriends() {

    }

    @Override
    public void fetchMyUser() {
    }

    private Realm getRealm() {
        Realm realm = Realm.getDefaultInstance();
        realm.setAutoRefresh(true);
        return realm;
    }
}
