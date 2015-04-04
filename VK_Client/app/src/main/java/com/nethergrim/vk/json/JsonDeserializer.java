package com.nethergrim.vk.json;

import com.nethergrim.vk.caching.models.ConversationsList;

/**
 * Created by nethergrim on 04.04.2015.
 */
public interface JsonDeserializer {

    public ConversationsList getConversations(String s);
}
