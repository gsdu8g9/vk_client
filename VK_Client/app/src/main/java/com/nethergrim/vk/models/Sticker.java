package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sticker extends RealmObject {

    @JsonProperty("id")
    private String id;

    @JsonProperty("product_id")
    /**
     * идентификатор набора;
     * */
    private String productId;

    @JsonProperty("photo_64")
    private String photo64;

    @JsonProperty("photo_128")
    private String photo128;

    @JsonProperty("photo_256")
    private String photo256;

    @JsonProperty("photo_352")
    private String photo352;

    @JsonProperty("width")
    private int width;

    @JsonProperty("height")
    private int height;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPhoto64() {
        return photo64;
    }

    public void setPhoto64(String photo64) {
        this.photo64 = photo64;
    }

    public String getPhoto128() {
        return photo128;
    }

    public void setPhoto128(String photo128) {
        this.photo128 = photo128;
    }

    public String getPhoto256() {
        return photo256;
    }

    public void setPhoto256(String photo256) {
        this.photo256 = photo256;
    }

    public String getPhoto352() {
        return photo352;
    }

    public void setPhoto352(String photo352) {
        this.photo352 = photo352;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
