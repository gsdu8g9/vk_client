package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;

/**
 * @author andrej on 04.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostSource extends RealmObject {

    /**
     * "type":"api"
     */
    @JsonProperty("type")
    private String type;

    /**
     * "platform":"instagram"
     */
    @JsonProperty("platform")
    private String platform;

    /**
     * "url":"https:\/\/instagram.com\/p\/56b3wZThCl\/"
     */
    @JsonProperty("url")
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
