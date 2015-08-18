package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author andrej on 05.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListOfMessages extends WebResponse {

    @JsonProperty("count")
    private int count;
    @JsonProperty("unread")
    private int unread;
    @JsonProperty("items")
    private List<Message> messages;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
