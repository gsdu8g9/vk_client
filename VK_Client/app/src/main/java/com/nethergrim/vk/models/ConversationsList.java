package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by nethergrim on 04.04.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationsList {

    @JsonProperty("count")
    private long count;

    @JsonProperty("unread_dialogs")
    private int unreadCount;

    @JsonProperty("items")
    private ArrayList<Conversation> results;

    public ConversationsList() {
    }


    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public ArrayList<Conversation> getResults() {
        return results;
    }

    public void setResults(ArrayList<Conversation> results) {
        this.results = results;
    }
}
