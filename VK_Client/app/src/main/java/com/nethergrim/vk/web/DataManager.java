package com.nethergrim.vk.web;

import java.util.List;

/**
 * @author andrej on 24.07.15.
 */
public interface DataManager {

    void manageConversationsAndUsers(int limit, int offset, boolean onlyUnread);

    void manageUsers(List<Long> userIds);

}
