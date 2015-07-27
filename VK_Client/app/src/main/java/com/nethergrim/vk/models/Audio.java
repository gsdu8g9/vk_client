package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;

/**
 * @author andrej on 27.07.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Audio extends RealmObject {

    @JsonProperty("id")
    private long id;

    @JsonProperty("owner_id")
    private long ownerId;

    @JsonProperty("artist")
    private String artist;

    @JsonProperty("title")
    private String title;

    @JsonProperty("duration")
    private long duration;

    @JsonProperty("url")
    private String url;

    @JsonProperty("lyrics_id")
    private long lyricsId;

    @JsonProperty("album_id")
    private long albumId;

    @JsonProperty("genre_id")
    private long genreId;

    @JsonProperty("date")
    private long date;

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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getLyricsId() {
        return lyricsId;
    }

    public void setLyricsId(long lyricsId) {
        this.lyricsId = lyricsId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
