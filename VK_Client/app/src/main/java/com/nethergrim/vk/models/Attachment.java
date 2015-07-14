package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String type;

    public Attachment() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
