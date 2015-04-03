package com.nethergrim.vk.caching.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class Conversation extends RealmObject {

    private long unreadCount;
    private Message lastMessage;
    @PrimaryKey
    private long userId;

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
