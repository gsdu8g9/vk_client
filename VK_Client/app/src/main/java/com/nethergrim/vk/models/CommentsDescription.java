package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;

/**
 * @author andrej on 04.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentsDescription extends RealmObject {


    /**
     * количество комментариев
     */
    @JsonProperty("count")
    private int count;

    /**
     * может ли текущий пользователь оставлять комментарии (1 — может, 0 — не может, возвращается
     * только для desktop-приложений);
     */
    @JsonProperty("can_post")
    private int canPost;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCanPost() {
        return canPost;
    }

    public void setCanPost(int canPost) {
        this.canPost = canPost;
    }
}
