package com.nethergrim.vk.models;

import io.realm.RealmObject;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 15.10.15.
 */
public class RealmLong extends RealmObject {

    private long data;

    public RealmLong() {
    }

    public RealmLong(long data) {
        this.data = data;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}
