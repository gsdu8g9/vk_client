package com.nethergrim.vk.web;

import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfUsers;

import java.util.List;

/**
 * @author andreydrobyazko on 4/3/15.
 */

public interface WebRequestManager {

    public void getConversations(int limit, int offset, boolean onlyUnread, int previewLenght, final WebCallback<ConversationsList> callback);

    public void getUsers(List<Long> ids, List<String> fields, String nameCase, WebCallback<ListOfUsers> callback);

    public void getUsersForConversations(ConversationsList list, WebCallback<ListOfUsers> callback);
}
