package com.nethergrim.vk.event;

import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfMessages;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com).
 *         All rights reserved!
 */
public class ConversationUpdatedEvent extends ConversationsUserObject {

    private long mChatId;
    private String mUserId;
    private long mCount;

    public ConversationUpdatedEvent(ListOfMessages listOfMessages, String userId, long chatid) {
        this.mCount = listOfMessages.getCount();
        this.mUserId = userId;
        this.mChatId = chatid;
    }

    public long getChatId() {
        return mChatId;
    }

    public void setChatId(long chatId) {
        mChatId = chatId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public long getCount() {
        return mCount;
    }

    public void setCount(long count) {
        mCount = count;
    }
}
