package com.nethergrim.vk.models.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 23.07.15.
 */
@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushMessage extends PushObject {

//        Bundle[{msg_id=8345, uid=69621580, text=lol, type=msg, badge=1, collapse_key=msg}]

    @JsonProperty("msg_id")

    String msgId;
    @JsonProperty("uid")
    String uid;
    @JsonProperty("text")
    String text;
    @JsonProperty("type")
    String type;
    @JsonProperty("badge")
    String badge;
    @JsonProperty("collapse_key")
    String collapseKey;
    public PushMessage() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public long getUserId() {
        return Long.parseLong(uid);
    }

    public String getCollapseKey() {
        return collapseKey;
    }

    public void setCollapseKey(String collapseKey) {
        this.collapseKey = collapseKey;
    }

    @Override
    public PushType getPushType() {
        return PushType.Message;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "msgId='" + msgId + '\'' +
                ", uid='" + uid + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", badge='" + badge + '\'' +
                ", collapseKey='" + collapseKey + '\'' +
                '}';
    }
}
