package com.nethergrim.vk.web;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Should be used from Ui Thread, to fetch data from the backend, and persist it to the database.
 * After that on the Ui Thread Subscriber should be notified with {@link com.squareup.otto.Bus}
 * Inside, it will call web requests in {@link android.app.Service} in the background thread, to
 * process and persist all the data.
 *
 * @author andrej on 24.07.15.
 */
public class RealmDataManagerImpl implements DataManager {


    @Override
    public void fetchConversationsAndUsers(int limit, int offset, boolean onlyUnread) {

    }

    @Override
    public void fetchUsers(@NonNull List<Long> userIds) {

    }

    @Override
    public void fetchMyFriends() {

    }

    @Override
    public void fetchMyUser() {

    }

}
