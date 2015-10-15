package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 15.10.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "id",
        "type",
        "purchased",
        "active",
        "purchase_date",
        "title",
        "base_url",
        "stickers"
})
public class Product {

    @JsonProperty("id")
    private long id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("purchased")
    private long purchased;
    @JsonProperty("active")
    private long active;
    @JsonProperty("purchase_date")
    private long purchaseDate;
    @JsonProperty("title")
    private String title;
    @JsonProperty("base_url")
    private String baseUrl;
    @JsonProperty("stickers")
    private Stickers stickers;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The id
     */
    @JsonProperty("id")
    public long getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The purchased
     */
    @JsonProperty("purchased")
    public long getPurchased() {
        return purchased;
    }

    /**
     * @param purchased The purchased
     */
    @JsonProperty("purchased")
    public void setPurchased(long purchased) {
        this.purchased = purchased;
    }

    /**
     * @return The active
     */
    @JsonProperty("active")
    public long getActive() {
        return active;
    }

    /**
     * @param active The active
     */
    @JsonProperty("active")
    public void setActive(long active) {
        this.active = active;
    }

    /**
     * @return The purchaseDate
     */
    @JsonProperty("purchase_date")
    public long getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * @param purchaseDate The purchase_date
     */
    @JsonProperty("purchase_date")
    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * @return The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

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
     * @return The stickers
     */
    @JsonProperty("stickers")
    public Stickers getStickers() {
        return stickers;
    }

    /**
     * @param stickers The stickers
     */
    @JsonProperty("stickers")
    public void setStickers(Stickers stickers) {
        this.stickers = stickers;
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
