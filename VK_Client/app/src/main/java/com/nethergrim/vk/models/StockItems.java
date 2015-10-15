package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@agmail.com) on 15.10.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockItems extends WebResponse {

    @JsonProperty("count")
    public int count;

    @JsonProperty("items")
    public List<StockItem> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<StockItem> getItems() {
        return items;
    }

    public void setItems(List<StockItem> items) {
        this.items = items;
    }
}
