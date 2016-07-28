package com.nethergrim.vk.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author andrej on 30.07.15.
 */
public class UserPalette extends RealmObject {

    @PrimaryKey
    private long userId;

    private int vibrant;
    private int vibrantDark;
    private int vibrantLight;
    private int muted;
    private int mutedDark;
    private int mutedLight;

    private long generatedAt;

    public long getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(long generatedAt) {
        this.generatedAt = generatedAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getVibrant() {
        return vibrant;
    }

    public void setVibrant(int vibrant) {
        this.vibrant = vibrant;
    }

    public int getVibrantDark() {
        return vibrantDark;
    }

    public void setVibrantDark(int vibrantDark) {
        this.vibrantDark = vibrantDark;
    }

    public int getVibrantLight() {
        return vibrantLight;
    }

    public void setVibrantLight(int vibrantLight) {
        this.vibrantLight = vibrantLight;
    }

    public int getMuted() {
        return muted;
    }

    public void setMuted(int muted) {
        this.muted = muted;
    }

    public int getMutedDark() {
        return mutedDark;
    }

    public void setMutedDark(int mutedDark) {
        this.mutedDark = mutedDark;
    }

    public int getMutedLight() {
        return mutedLight;
    }

    public void setMutedLight(int mutedLight) {
        this.mutedLight = mutedLight;
    }
}
