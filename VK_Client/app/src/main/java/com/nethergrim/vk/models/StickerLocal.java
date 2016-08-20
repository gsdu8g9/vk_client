package com.nethergrim.vk.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * Created on 21.08.16.
 */

@RealmClass
public class StickerLocal implements RealmModel, Parcelable {


    public static final Creator<StickerLocal> CREATOR = new Creator<StickerLocal>() {
        @Override
        public StickerLocal createFromParcel(Parcel in) {
            return new StickerLocal(in);
        }

        @Override
        public StickerLocal[] newArray(int size) {
            return new StickerLocal[size];
        }
    };
    private long id;
    private String url;

    public StickerLocal() {
    }

    public StickerLocal(long id, String url) {
        this.id = id;
        this.url = url;
    }

    protected StickerLocal(Parcel in) {
        id = in.readLong();
        url = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(url);
    }
}
