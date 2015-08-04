package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;

/**
 * @author andrej on 04.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Doc extends RealmObject {

    /**
     * идентификатор документа.
     * положительное число
     */
    @JsonProperty("id")
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

}
