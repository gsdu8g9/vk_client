package com.nethergrim.vk.web;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import java.util.ArrayList;

/**
 * Should be used from Ui Thread, to fetch data from the backend, and persist it to the database.
 * After that on the Ui Thread Subscriber should be notified with {@link com.squareup.otto.Bus}
 * Inside, it will call web requests in {@link android.app.Service} in the background thread, to
 * process and persist all the data.
 *
 * @author andrej on 24.07.15.
 */
public interface DataManager {

    @UiThread
    void fetchConversationsAndUsers(int count, int offset, boolean onlyUnread);

    @UiThread
    void fetchUsers(@NonNull ArrayList<Long> userIds);

    @UiThread
    void fetchMyFriends(int count, int offset);

    @UiThread
    void fetchMyUser();

    @UiThread
    /**
     * This method will register to push-notifications, register online for 15 minutes, and fetch
     * list of users.
     * */
    void launchStartupTasks();

}
