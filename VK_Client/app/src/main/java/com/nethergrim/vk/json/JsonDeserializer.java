package com.nethergrim.vk.json;

import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfUsers;

/**
 * Created by nethergrim on 04.04.2015.
 */
public interface JsonDeserializer {

    public ConversationsList getConversations(String s);

    public ListOfUsers getListOfUsers(String s);
}
