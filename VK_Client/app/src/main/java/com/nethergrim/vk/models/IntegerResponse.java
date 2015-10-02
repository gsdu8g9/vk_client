package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 27.09.15.
 */
public class IntegerResponse extends WebResponse {

    @JsonProperty("response")
    private int mResponse;

    public int getResponse() {
        return mResponse;
    }

    public void setResponse(int response) {
        mResponse = response;
    }
}
