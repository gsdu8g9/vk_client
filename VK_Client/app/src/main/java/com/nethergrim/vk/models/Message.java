package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author andreydrobyazko on 3/20/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message extends RealmObject {

    @PrimaryKey
    private long id;

    private long user_id;
    private long fromId;
    private long date;
    private boolean read_state;
    private boolean out;
    private String title = "";
    private String body = "";
    private boolean emoji;
    private boolean important;
    private boolean deleted;

    public Message() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public boolean isRead_state() {
        return read_state;
    }

    public void setRead_state(boolean read_state) {
        this.read_state = read_state;
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
}
