package com.nethergrim.vk.caching.models;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class Conversation extends AbstractModel {

    private int unreadCount;
    private Message lastMessage;

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
