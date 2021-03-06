package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 15.10.15.
 */
public class StockItemsResponse extends WebResponse {

    @JsonProperty("response")
    private StockItems mStockItems;

    public StockItems getStockItems() {
        return mStockItems;
    }

    public void setStockItems(StockItems stockItems) {
        mStockItems = stockItems;
    }
}
