package com.nethergrim.vk.web;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author andrej on 24.07.15.
 */
public interface DataManager {

    void fetchConversationsAndUsers(int limit, int offset, boolean onlyUnread);

    void fetchUsers(@NonNull List<Long> userIds);

    void fetchMyFriends();

    void fetchMyUser();

}
