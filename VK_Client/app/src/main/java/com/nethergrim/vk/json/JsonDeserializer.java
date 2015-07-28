package com.nethergrim.vk.json;

import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfFriendIds;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.UserResult;
import com.nethergrim.vk.models.push.PushMessage;

import org.json.JSONObject;

/**
 * Created by nethergrim on 04.04.2015.
 */
public interface JsonDeserializer {

    ConversationsList getConversations(String s);

    ListOfUsers getListOfUsers(String s);

    UserResult getUser(String s);

    PushMessage getPushMessage(JSONObject jsonObject);

    ListOfFriendIds getFriendsIds(String s);
}
