package com.nethergrim.vk.web;

import com.nethergrim.vk.caching.models.Conversation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andreydrobyazko on 4/3/15.
 */
public class WebRequestManagerImpl implements WebRequestManager {

    @Override
    public List<Conversation> getConversations(int limit, int offset, boolean onlyUnread, int previewLenght) {
        return new ArrayList<>();
    }


}
