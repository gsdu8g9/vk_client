package com.nethergrim.vk.models;

import java.util.ArrayList;

/**
 * Created by nethergrim on 04.04.2015.
 */
public class ConversationsList {

    private long count;
    private int unread_dialogs;
    private ArrayList<Conversation> items;

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

    public ArrayList<Conversation> getItems() {
        return items;
    }

    public void setItems(ArrayList<Conversation> items) {
        this.items = items;
    }
}
