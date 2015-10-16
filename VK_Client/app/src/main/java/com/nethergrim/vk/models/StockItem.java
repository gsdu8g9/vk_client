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
        "product",
        "description",
        "author",
        "can_purchase",
        "free",
        "payment_type",
        "photo_35",
        "photo_70",
        "photo_140",
        "photo_296",
        "photo_592",
        "background",
        "demo_photos_560"
})
public class StockItem {

    @JsonProperty("product")
    private Product product;
    @JsonProperty("description")
    private String description;
    @JsonProperty("author")
    private String author;
    @JsonProperty("can_purchase")
    private long canPurchase;
    @JsonProperty("free")
    private long free;
    @JsonProperty("payment_type")
    private String paymentType;
    @JsonProperty("photo_35")
    private String photo35;
    @JsonProperty("photo_70")
    private String photo70;
    @JsonProperty("photo_140")
    private String photo140;
    @JsonProperty("photo_296")
    private String photo296;
    @JsonProperty("photo_592")
    private String photo592;
    @JsonProperty("background")
    private String background;
    @JsonProperty("demo_photos_560")
    private List<String> demoPhotos560 = new ArrayList<String>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The product
     */
    @JsonProperty("product")
    public Product getProduct() {
        return product;
    }

    /**
     * @param product The product
     */
    @JsonProperty("product")
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The author
     */
    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    /**
     * @param author The author
     */
    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return The canPurchase
     */
    @JsonProperty("can_purchase")
    public long getCanPurchase() {
        return canPurchase;
    }

    /**
     * @param canPurchase The can_purchase
     */
    @JsonProperty("can_purchase")
    public void setCanPurchase(long canPurchase) {
        this.canPurchase = canPurchase;
    }

    /**
     * @return The free
     */
    @JsonProperty("free")
    public long getFree() {
        return free;
    }

    /**
     * @param free The free
     */
    @JsonProperty("free")
    public void setFree(long free) {
        this.free = free;
    }

    /**
     * @return The paymentType
     */
    @JsonProperty("payment_type")
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType The payment_type
     */
    @JsonProperty("payment_type")
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return The photo35
     */
    @JsonProperty("photo_35")
    public String getPhoto35() {
        return photo35;
    }

    /**
     * @param photo35 The photo_35
     */
    @JsonProperty("photo_35")
    public void setPhoto35(String photo35) {
        this.photo35 = photo35;
    }

    /**
     * @return The photo70
     */
    @JsonProperty("photo_70")
    public String getPhoto70() {
        return photo70;
    }

    /**
     * @param photo70 The photo_70
     */
    @JsonProperty("photo_70")
    public void setPhoto70(String photo70) {
        this.photo70 = photo70;
    }

    /**
     * @return The photo140
     */
    @JsonProperty("photo_140")
    public String getPhoto140() {
        return photo140;
    }

    /**
     * @param photo140 The photo_140
     */
    @JsonProperty("photo_140")
    public void setPhoto140(String photo140) {
        this.photo140 = photo140;
    }

    /**
     * @return The photo296
     */
    @JsonProperty("photo_296")
    public String getPhoto296() {
        return photo296;
    }

    /**
     * @param photo296 The photo_296
     */
    @JsonProperty("photo_296")
    public void setPhoto296(String photo296) {
        this.photo296 = photo296;
    }

    /**
     * @return The photo592
     */
    @JsonProperty("photo_592")
    public String getPhoto592() {
        return photo592;
    }

    /**
     * @param photo592 The photo_592
     */
    @JsonProperty("photo_592")
    public void setPhoto592(String photo592) {
        this.photo592 = photo592;
    }

    /**
     * @return The background
     */
    @JsonProperty("background")
    public String getBackground() {
        return background;
    }

    /**
     * @param background The background
     */
    @JsonProperty("background")
    public void setBackground(String background) {
        this.background = background;
    }

    /**
     * @return The demoPhotos560
     */
    @JsonProperty("demo_photos_560")
    public List<String> getDemoPhotos560() {
        return demoPhotos560;
    }

    /**
     * @param demoPhotos560 The demo_photos_560
     */
    @JsonProperty("demo_photos_560")
    public void setDemoPhotos560(List<String> demoPhotos560) {
        this.demoPhotos560 = demoPhotos560;
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
