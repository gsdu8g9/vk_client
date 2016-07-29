package com.nethergrim.vk.models;

import io.realm.RealmObject;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 14.08.15.
 */
public class RealmString extends RealmObject {

    private String s;

    public RealmString() {
    }

    public RealmString(String s) {

        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
