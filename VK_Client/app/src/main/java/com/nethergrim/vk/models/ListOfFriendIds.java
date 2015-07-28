package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author andrej on 28.07.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListOfFriendIds {

    @JsonProperty("count")
    private int count;

    @JsonProperty("items")
    private List<Long> ids;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
