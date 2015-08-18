package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author andrej on 18.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebError {

    @JsonProperty("error_code")
    private String mErrorCode;

    @JsonProperty("error_msg")
    private String mErrorMessage;

    public String getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(String errorCode) {
        mErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "Error{" +
                "mErrorCode='" + mErrorCode + '\'' +
                ", mErrorMessage='" + mErrorMessage + '\'' +
                '}';
    }
}
