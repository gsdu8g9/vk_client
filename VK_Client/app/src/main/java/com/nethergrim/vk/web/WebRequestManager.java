package com.nethergrim.vk.web;

import com.nethergrim.vk.caching.models.Conversation;

import java.util.List;

/**
 * @author andreydrobyazko on 4/3/15.
 */

public interface WebRequestManager {

    public void getConversations(int limit, int offset, boolean onlyUnread, int previewLenght, WebCallback<List<Conversation>> callback);
}
