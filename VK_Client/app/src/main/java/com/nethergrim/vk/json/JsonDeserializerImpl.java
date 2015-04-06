package com.nethergrim.vk.json;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfUsers;

import java.io.IOException;


/**
 * Created by nethergrim on 04.04.2015.
 */
public class JsonDeserializerImpl implements JsonDeserializer {

    private ObjectMapper mapper;
    private static final String TAG = JsonDeserializerImpl.class.getName();

    public JsonDeserializerImpl() {
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
}
