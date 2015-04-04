package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author andreydrobyazko on 3/20/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversation extends RealmObject {

    private long unread;
    private Message message;

    @PrimaryKey
    private long user_id;

    public Conversation() {
    }

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

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
