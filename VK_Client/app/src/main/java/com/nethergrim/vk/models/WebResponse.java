package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author andrej on 18.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebResponse {

    @JsonProperty("error")
    private WebError mError;

    public boolean ok(){
        return mError == null;
    }

    public WebError getError() {
        return mError;
    }
}
