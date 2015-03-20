package com.nethergrim.vk.caching.models;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class Conversation extends AbstractModel {

    private long unreadCount;
    private Message lastMessage;

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
}
