package com.nethergrim.vk.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.ListOfUsers;

import java.util.List;

/**
 * @author andrej on 24.07.15.
 */
public interface DataManager {

    void manageConversationsAndUsers(int limit, int offset, boolean onlyUnread);

    void manageUsers(@NonNull List<Long> userIds);

    void manageUsers(@NonNull List<Long> userIds, @Nullable WebCallback<ListOfUsers> callback);

    void manageFriends(@Nullable WebCallback<ListOfUsers> callback);

    void fetchMyUser();

}
