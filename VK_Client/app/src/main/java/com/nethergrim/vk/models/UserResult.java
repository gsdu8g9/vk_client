package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResult {

    private User response;

    public User getResponse() {
        return response;
    }

    public void setResponse(User response) {
        this.response = response;
    }
}
