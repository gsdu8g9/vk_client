package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 15.10.15.
 */
public class StickersResponse extends WebResponse {

    @JsonProperty("response")
    private StickersList mStockItems;

    public StickersList getStockItems() {
        return mStockItems;
    }
}
