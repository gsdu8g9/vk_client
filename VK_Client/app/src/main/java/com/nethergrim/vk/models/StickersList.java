package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@agmail.com) on 15.10.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StickersList extends WebResponse {

    @JsonProperty("count")
    public int count;

    @JsonProperty("items")
    private List<StickersCollection> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<StickersCollection> getItems() {
        return items;
    }

}
