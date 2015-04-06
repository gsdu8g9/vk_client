package com.nethergrim.vk.models;

import java.util.List;

/**
 * @author andreydrobyazko on 4/6/15.
 */
public class ListOfUsers {

    private List<User> response;

    public List<User> getResponse() {
        return response;
    }

    public void setResponse(List<User> response) {
        this.response = response;
    }
}
