package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 12.09.15.
 */
public class ListOfMessagesResponse extends WebResponse {

    @JsonProperty("response")
    private ListOfMessages mListOfMessages;

    @Override
    public String toString() {
        return "ListOfMessagesResponse{" +
                "mListOfMessages=" + mListOfMessages +
                '}';
    }

    public ListOfMessages getListOfMessages() {
        return mListOfMessages;
    }

    public void setListOfMessages(ListOfMessages listOfMessages) {
        mListOfMessages = listOfMessages;
    }
}
