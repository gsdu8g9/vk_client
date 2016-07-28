package com.nethergrim.vk.caching;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@SuppressWarnings("unused")
public class LongToLongModel {

    private static final ObjectMapper mapper = new ObjectMapper();
    @JsonProperty("l1")
    private long l1;
    @JsonProperty("l2")
    private long l2;

    public LongToLongModel() {
    }

    public LongToLongModel(long l1, long l2) {
        this.l1 = l1;
        this.l2 = l2;
    }

    @Nullable
    public static LongToLongModel fromString(@NonNull String s) {
        try {
            return mapper.readValue(s, LongToLongModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongToLongModel that = (LongToLongModel) o;

        return l1 == that.l1;

    }

    @Override
    public int hashCode() {
        return (int) (l1 ^ (l1 >>> 32));
    }

    public long getL1() {
        return l1;
    }

    public void setL1(long l1) {
        this.l1 = l1;
    }

    public long getL2() {
        return l2;
    }

    public void setL2(long l2) {
        this.l2 = l2;
    }
}
