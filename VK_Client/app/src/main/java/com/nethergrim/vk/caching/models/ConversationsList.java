package com.nethergrim.vk.caching.models;

import java.util.List;

/**
 * Created by nethergrim on 04.04.2015.
 */
public class ConversationsList {

    private long count;
    private int unread_dialogs;
    private List<Conversation> items;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getUnread_dialogs() {
        return unread_dialogs;
    }

    public void setUnread_dialogs(int unread_dialogs) {
        this.unread_dialogs = unread_dialogs;
    }

    public List<Conversation> getItems() {
        return items;
    }

    public void setItems(List<Conversation> items) {
        this.items = items;
    }
}
