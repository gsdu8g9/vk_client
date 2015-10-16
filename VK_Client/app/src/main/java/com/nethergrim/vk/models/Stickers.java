package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 15.10.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "base_url",
        "sticker_ids"
})
public class Stickers {

    @JsonProperty("base_url")
    private String baseUrl;
    @JsonProperty("sticker_ids")
    private List<Long> stickerIds = new ArrayList<Long>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The baseUrl
     */
    @JsonProperty("base_url")
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * @param baseUrl The base_url
     */
    @JsonProperty("base_url")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * @return The stickerIds
     */
    @JsonProperty("sticker_ids")
    public List<Long> getStickerIds() {
        return stickerIds;
    }

    /**
     * @param stickerIds The sticker_ids
     */
    @JsonProperty("sticker_ids")
    public void setStickerIds(List<Long> stickerIds) {
        this.stickerIds = stickerIds;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
