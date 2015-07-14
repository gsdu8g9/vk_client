package com.nethergrim.vk.models;

import io.realm.RealmObject;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class PushSettings extends RealmObject {

    private int sound;
    private int disabled_until;


    public PushSettings(int sound, int disabled_until) {
        this.sound = sound;
        this.disabled_until = disabled_until;
    }
}
