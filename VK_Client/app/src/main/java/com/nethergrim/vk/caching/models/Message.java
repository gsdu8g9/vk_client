package com.nethergrim.vk.caching.models;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class Message extends AbstractModel {

    long id;
    long userId;
    long fromId;
    long date;
    boolean isRead;
    boolean out;
    String title = "";
    String body = "";
    boolean emoji;
    boolean important;
    boolean deleted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isEmoji() {
        return emoji;
    }

    public void setEmoji(boolean emoji) {
        this.emoji = emoji;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    //    id: 360658,
//    date: 1394122091,
//    out: 0,
//    user_id: 85635407,
//    read_state: 0,
//    title: ' ... ',
//    body: 'hey'
}
