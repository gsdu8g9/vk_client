package com.nethergrim.vk.models;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 3/20/15.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@JsonIgnoreProperties(ignoreUnknown = true)
@RealmClass
public class Conversation implements RealmModel {

    private static final String TAG = "Conversation";

    /**
     * count of unread messages in this conversation
     */
    private long unread;
    private Message message;

    @PrimaryKey
    /**
     * Id of the user, in case 1-1 conversation, Id of a group chat in case of group chat.
     * */
    private long id;
    private long date;


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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isAGroupChat(){
        if (message == null){
            Log.e(TAG, "isAGroupChat: MESSAGE IS NULL");
            return false;
        }
        return message.isAGroupChat();
    }

    public long getPeerId(){
        if (message == null){
            Log.e(TAG, "isAGroupChat: MESSAGE IS NULL");
            return -1;
        }
        return message.getPeerId();
    }
}
