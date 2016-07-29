package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 18.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebResponse {

    @JsonProperty("error")
    protected WebError mError;

    public boolean ok(){
        return mError == null;
    }

    public WebError getError() {
        return mError;
    }

    @Override
    public String toString() {
        if (mError != null){
            return mError.toString();
        }
        return super.toString();
    }
}
