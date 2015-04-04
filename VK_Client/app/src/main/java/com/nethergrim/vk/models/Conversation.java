package com.nethergrim.vk.models;

import io.realm.RealmObject;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class Conversation extends RealmObject {

    private long unread;
    private Message message;

    public long getUnread() {
        return unread;
    }

    public void setUnread(long unread) {
        this.unread = unread;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
