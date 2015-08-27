package com.nethergrim.vk.models.push;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author andrej on 23.07.15.
 */
public class PushMessage extends PushObject {

//        Bundle[{msg_id=8345, uid=69621580, text=lol, type=msg, badge=1, collapse_key=msg}]

    @JsonProperty("msg_id")
    private String msgId;
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("text")
    private String text;
    @JsonProperty("type")
    private String type;
    @JsonProperty("badge")
    private String badge;
    @JsonProperty("collapse_key")
    private String collapseKey;

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

    public long getUserId(){
        return Long.parseLong(uid);
    }

    public void setBadge(String badge) {
        this.badge = badge;
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
}
