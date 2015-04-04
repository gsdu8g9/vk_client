package com.nethergrim.vk.web;

import com.nethergrim.vk.caching.models.ConversationsList;
import com.nethergrim.vk.callbacks.WebCallback;

/**
 * @author andreydrobyazko on 4/3/15.
 */

public interface WebRequestManager {

    public void getConversations(int limit, int offset, boolean onlyUnread, int previewLenght, final WebCallback<ConversationsList> callback);
}
