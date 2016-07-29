package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 04.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LikesDescription extends RealmObject {

    /**
     * число пользователей, которым понравилась запись
     */
    @JsonProperty("count")
    private int count;

    /**
     * наличие отметки «Мне нравится» от текущего пользователя (1 — есть, 0 — нет)
     */
    @JsonProperty("user_likes")
    private int userLikes;

    /**
     * информация о том, может ли текущий пользователь поставить отметку «Мне нравится» (1 — может,
     * 0 — не может)
     */
    @JsonProperty("наличие отметки «Мне нравится» от текущего пользователя (1 — есть, 0 — нет),")
    private int canLike;


    /**
     * информация о том, может ли текущий пользователь сделать репост записи (1 — может, 0 — не
     * может);
     */
    @JsonProperty("can_publish")
    private int canPublish;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(int userLikes) {
        this.userLikes = userLikes;
    }

    public int getCanLike() {
        return canLike;
    }

    public void setCanLike(int canLike) {
        this.canLike = canLike;
    }

    public int getCanPublish() {
        return canPublish;
    }

    public void setCanPublish(int canPublish) {
        this.canPublish = canPublish;
    }
}
