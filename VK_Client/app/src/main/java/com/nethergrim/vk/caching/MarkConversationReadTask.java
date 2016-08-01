package com.nethergrim.vk.caching;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@SuppressWarnings("unused")
@RealmClass
public class MarkConversationReadTask implements RealmModel {


    @JsonProperty("conversationId")
    @PrimaryKey
    private long conversationId;
    @JsonProperty("toTime")
    private long toTime;


    public MarkConversationReadTask() {
    }

    public MarkConversationReadTask(long l1, long l2) {
        this.conversationId = l1;
        this.toTime = l2;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public long getToTime() {
        return toTime;
    }

    public void setToTime(long toTime) {
        this.toTime = toTime;
    }
}
