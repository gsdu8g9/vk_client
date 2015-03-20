package com.nethergrim.vk.web;

import com.nethergrim.vk.caching.models.Conversation;
import com.nethergrim.vk.caching.models.Message;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class WebResponseMapper {

    public static List<Conversation> getConversations(VKResponse vkResponse) {
        List<Conversation> result = new ArrayList<>();
        try {
            JSONObject response = vkResponse.json.getJSONObject("response");
            long count = response.getLong("count");
            JSONArray items = response.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject json = items.getJSONObject(i);
                Conversation conversation = new Conversation();
                if (json.has("unread")) {
                    conversation.setUnreadCount(json.getLong("unread"));
                }
                if (json.has("message")) {
                    conversation.setLastMessage(getMessage(json.getJSONObject("message")));
                    conversation.setUserId(conversation.getLastMessage().getUserId());
                }
                result.add(conversation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Message getMessage(JSONObject jsonObject) {
        Message message = new Message();
        try {
            message.setId(jsonObject.getLong("id"));
            message.setDate(jsonObject.getLong("date"));
            message.setOut(jsonObject.getInt("out") == 1);
            message.setUserId(jsonObject.getLong("user_id"));
            message.setRead(jsonObject.getInt("read_state") == 1);
            message.setTitle(jsonObject.getString("title"));
            message.setBody(jsonObject.getString("body"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

}
