package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo extends RealmObject {
    @PrimaryKey
    private long id;
    /**
     * идентификатор альбома, в котором находится фотография.
     * int (числовое значение)
     */
    private long album_id;
    /**
     * идентификатор владельца фотографии.
     * int (числовое значение)
     */
    private long owner_id;
    /**
     * идентификатор пользователя, загрузившего фото (если фотография размещена в сообществе). Для фотографий, размещенных от имени сообщества, user_id=100.
     * положительное число
     */
    private long user_id;
    /**
     * url копии фотографии с максимальным размером 75x75px.
     */
    private String photo_75;
    /**
     * url копии фотографии с максимальным размером 130x130px.
     */
    private String photo_130;
    /**
     * url копии фотографии с максимальным размером 604x604px.
     */
    private String photo_604;
    /**
     * url копии фотографии с максимальным размером 807x807px.
     */
    private String photo_807;
    /**
     * url копии фотографии с максимальным размером 1280x1024px.
     */
    private String photo_1280;
    /**
     * url копии фотографии с максимальным размером 2560x2048px.
     */
    private String photo_2560;
    /**
     * ширина оригинала фотографии в пикселах.
     * положительное число
     */
    private int width;
    /**
     * высота оригинала фотографии в пикселах.
     * положительное число
     */
    private int height;
    /**
     * текст описания фотографии.
     */
    private String text;
    /**
     * дата добавления в формате unixtime.
     * положительное число
     */
    private long date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(long owner_id) {
        this.owner_id = owner_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getPhoto_75() {
        return photo_75;
    }

    public void setPhoto_75(String photo_75) {
        this.photo_75 = photo_75;
    }

    public String getPhoto_130() {
        return photo_130;
    }

    public void setPhoto_130(String photo_130) {
        this.photo_130 = photo_130;
    }

    public String getPhoto_604() {
        return photo_604;
    }

    public void setPhoto_604(String photo_604) {
        this.photo_604 = photo_604;
    }

    public String getPhoto_807() {
        return photo_807;
    }

    public void setPhoto_807(String photo_807) {
        this.photo_807 = photo_807;
    }

    public String getPhoto_1280() {
        return photo_1280;
    }

    public void setPhoto_1280(String photo_1280) {
        this.photo_1280 = photo_1280;
    }

    public String getPhoto_2560() {
        return photo_2560;
    }

    public void setPhoto_2560(String photo_2560) {
        this.photo_2560 = photo_2560;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


}
