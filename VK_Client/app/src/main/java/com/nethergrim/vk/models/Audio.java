package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.realm.RealmObject;

/**
 * @author andrej on 27.07.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Audio extends RealmObject {

    private long id;

    private long owner_id;


    private String artist;

    private String title;

    private long duration;

    private String url;

    private long lyrics_id;

    private long album_id;

    private long genre_id;

    private long date;

}
