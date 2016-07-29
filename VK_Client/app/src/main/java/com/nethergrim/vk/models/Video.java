package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 04.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Video extends RealmObject {


    /**
     * идентификатор видеозаписи.
     */
    @PrimaryKey
    @JsonProperty("id")
    private long id;

    /**
     * идентификатор владельца видеозаписи.
     */
    @JsonProperty("owner_id")
    private long ownerId;

    /**
     * название видеозаписи.
     */
    @JsonProperty("title")
    private String title;

    /**
     * текст описания видеозаписи.
     */
    @JsonProperty("description")
    private String description;

    /**
     * длительность ролика в секундах.
     */
    @JsonProperty("duration")
    private long duration;

    @JsonProperty("photo_130")
    private String photo130;
    @JsonProperty("photo_320")
    private String photo320;
    @JsonProperty("photo_640")
    private String photo640;

    /**
     * дата создания видеозаписи в формате unixtime.
     */
    @JsonProperty("date")
    private long date;

    /**
     * дата добавления видеозаписи пользователем или группой в формате unixtime.
     */
    @JsonProperty("adding_date")
    private long addingDate;

    /**
     * количество просмотров видеозаписи
     */
    @JsonProperty("views")
    private long viewsCount;

    /**
     * количество комментариев к видеозаписи.
     */
    @JsonProperty("comments")
    private int commentsCount;

    /**
     * адрес страницы с плеером, который можно использовать для воспроизведения ролика в браузере.
     * Поддерживается flash и html5, плеер всегда масштабируется по размеру окна.
     */
    @JsonProperty("player")
    private String playerUrl;

    /**
     * ключ доступа к объекту. Подробнее см. https://vk.com/dev/access_key
     * При получении объектов, прямого доступа к которым может не быть, например, фотографий или
     * видео в новостях, вместе с объектами приходит поле access_key, которое необходимо передавать
     * при получении этих объектов напрямую или при совершении с ними действий.
     * <p/>
     * Например, поле access_key принимают методы video.get, photos.getById.
     */
    @JsonProperty("access_key")
    private String accessKey;

    /**
     * поле возвращается в том случае, если видеоролик находится в процессе обработки, всегда
     * содержит 1.
     */
    @JsonProperty("processing")
    private int processing;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPhoto130() {
        return photo130;
    }

    public void setPhoto130(String photo130) {
        this.photo130 = photo130;
    }

    public String getPhoto320() {
        return photo320;
    }

    public void setPhoto320(String photo320) {
        this.photo320 = photo320;
    }

    public String getPhoto640() {
        return photo640;
    }

    public void setPhoto640(String photo640) {
        this.photo640 = photo640;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(long addingDate) {
        this.addingDate = addingDate;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public int getProcessing() {
        return processing;
    }

    public void setProcessing(int processing) {
        this.processing = processing;
    }
}
