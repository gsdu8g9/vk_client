package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 04.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Doc extends RealmObject {

    /**
     * идентификатор документа.
     * положительное число
     */
    @JsonProperty("id")
    @PrimaryKey

    private long id;
    /**
     * идентификатор пользователя, загрузившего документ.
     * положительное число
     */
    @JsonProperty("owner_id")
    private long ownerId;
    /**
     * название документа.
     */
    @JsonProperty("title")
    private String title;
    /**
     * размер документа в байтах.
     */
    @JsonProperty("size")
    private long size;
    /**
     * расширение документа
     */
    @JsonProperty("ext")
    private String extension;
    /**
     * адрес документа, по которому его можно загрузить
     */
    @JsonProperty("url")
    private String url;
    /**
     * адрес изображения с размером 100x75px (если файл графический).
     */
    @JsonProperty("photo_100")
    private String photo100;
    /**
     * адрес изображения с размером 130x100px (если файл графический).
     */
    @JsonProperty("photo_130")
    private String photo130;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhoto100() {
        return photo100;
    }

    public void setPhoto100(String photo100) {
        this.photo100 = photo100;
    }

    public String getPhoto130() {
        return photo130;
    }

    public void setPhoto130(String photo130) {
        this.photo130 = photo130;
    }

}
