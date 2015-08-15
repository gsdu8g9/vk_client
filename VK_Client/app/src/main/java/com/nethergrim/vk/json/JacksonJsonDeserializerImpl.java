package com.nethergrim.vk.json;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.UserResult;
import com.nethergrim.vk.models.push.PushMessage;

import org.json.JSONObject;

import java.io.IOException;


/**
 * @author nethergrim on 04.04.2015.
 */
public class JacksonJsonDeserializerImpl implements JsonDeserializer {

    private static final String TAG = JacksonJsonDeserializerImpl.class.getName();
    private ObjectMapper mapper;

    public JacksonJsonDeserializerImpl() {
        mapper = new ObjectMapper();
    }

    @Override
    public ConversationsList getConversations(String s) {
        try {
            return mapper.readValue(s, ConversationsList.class);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public ListOfUsers getListOfUsers(String s) {
        try {
            return mapper.readValue(s, ListOfUsers.class);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public UserResult getUser(String s) {
        try {
            return mapper.readValue(s, UserResult.class);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public PushMessage getPushMessage(JSONObject jsonObject) {
        try {
            return mapper.readValue(jsonObject.toString(), PushMessage.class);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public ListOfFriendIds getFriendsIds(String s) {
        try {
            return mapper.readValue(s, ListOfFriendIds.class);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public ListOfMessages getListOfMessages(String s) {
        try {
            return mapper.readValue(s, ListOfMessages.class);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

}
