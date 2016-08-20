package com.nethergrim.vk.models.push;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 23.07.15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PushMessage extends PushObject implements Parcelable {

//        Bundle[{msg_id=8345, uid=69621580, text=lol, type=msg, badge=1, collapse_key=msg}]

    public static final Creator<PushMessage> CREATOR = new Creator<PushMessage>() {
        @Override
        public PushMessage createFromParcel(Parcel in) {
            return new PushMessage(in);
        }

        @Override
        public PushMessage[] newArray(int size) {
            return new PushMessage[size];
        }
    };
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

    protected PushMessage(Parcel in) {
        msgId = in.readString();
        uid = in.readString();
        text = in.readString();
        type = in.readString();
        badge = in.readString();
        collapseKey = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(msgId);
        parcel.writeString(uid);
        parcel.writeString(text);
        parcel.writeString(type);
        parcel.writeString(badge);
        parcel.writeString(collapseKey);
    }
}
