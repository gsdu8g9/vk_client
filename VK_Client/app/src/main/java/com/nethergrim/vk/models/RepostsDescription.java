package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;

/**
 * @author andrej on 04.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepostsDescription extends RealmObject {

    /**
     * число пользователей, скопировавших запись
     */
    @JsonProperty("count")
    private int count;
    /**
     * наличие репоста от текущего пользователя (1 — есть, 0 — нет);
     */
    @JsonProperty("user_reposted")
    private int userReposted;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUserReposted() {
        return userReposted;
    }

    public void setUserReposted(int userReposted) {
        this.userReposted = userReposted;
    }

}
