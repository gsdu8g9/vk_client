package com.nethergrim.vk.json;

import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfUsers;

/**
 * Created by nethergrim on 04.04.2015.
 */
public interface JsonDeserializer {

    ConversationsList getConversations(String s);

    ListOfUsers getListOfUsers(String s);
}
