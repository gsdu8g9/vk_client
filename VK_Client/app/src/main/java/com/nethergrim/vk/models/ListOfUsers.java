package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author andreydrobyazko on 4/6/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListOfUsers extends WebResponse {

    private List<User> response;

    public List<User> getResponse() {
        return response;
    }

    public void setResponse(List<User> response) {
        this.response = response;
    }

    @Override
    public String toString() {
        if (mError == null){ // valid response with no error
            return response.toString();
        } else {
            return super.toString();
        }
    }
}
