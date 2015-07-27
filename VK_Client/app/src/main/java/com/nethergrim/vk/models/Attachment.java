package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment extends RealmObject {

    /**
     * Возможны следующие значения поля type:
     * photo — фотография;
     * video — видеозапись;
     * audio — аудиозапись;
     * doc — документ;
     * wall — запись на стене;
     * wall_reply — комментарий к записи на стене;
     * sticker — стикер.
     */
    @JsonProperty("unread_dialogs")
    private String type;

    @JsonProperty("photo")
    private Photo photo;

    @JsonProperty("sticker")
    private Sticker sticker;
    public Attachment() {
    }

    public Sticker getSticker() {
        return sticker;
    }

    public void setSticker(Sticker sticker) {
        this.sticker = sticker;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
