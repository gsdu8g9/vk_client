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
    @JsonProperty("type")
    private String type;
    @JsonProperty("photo")
    private Photo photo;
    @JsonProperty("sticker")
    private Sticker sticker;
    @JsonProperty("audio")
    private Audio audio;
    @JsonProperty("wall")
    private Wall wall;
    @JsonProperty("wall_reply")
    private WallReply wallReply;
    @JsonProperty("doc")
    private Doc doc;
    @JsonProperty("video")
    private Video video;

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }

    public WallReply getWallReply() {
        return wallReply;
    }

    public void setWallReply(WallReply wallReply) {
        this.wallReply = wallReply;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
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
