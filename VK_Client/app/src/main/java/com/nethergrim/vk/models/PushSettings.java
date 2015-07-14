package com.nethergrim.vk.models;

import io.realm.RealmObject;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class PushSettings extends RealmObject {

    private int sound;
    private int disabled_until;


    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getDisabled_until() {
        return disabled_until;
    }

    public void setDisabled_until(int disabled_until) {
        this.disabled_until = disabled_until;
    }
}
