package com.nethergrim.vk.json;

import com.nethergrim.vk.caching.models.ConversationsList;

import org.boon.json.JsonParser;
import org.boon.json.JsonParserFactory;

/**
 * Created by nethergrim on 04.04.2015.
 */
public class JsonDeserializerImpl implements JsonDeserializer {

    @Override
    public ConversationsList getConversations(String s) {
        JsonParser parser = new JsonParserFactory().create();
        return (ConversationsList) parser.parse(s);
    }
}
