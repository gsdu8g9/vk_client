package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author andreydrobyazko on 4/6/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListOfUsers {

    private List<User> response;

    public List<User> getResponse() {
        return response;
    }

    public void setResponse(List<User> response) {
        this.response = response;
    }
}
