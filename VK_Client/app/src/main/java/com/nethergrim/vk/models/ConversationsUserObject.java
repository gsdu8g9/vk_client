package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author andrej on 14.08.15.
 */
public class ConversationsUserObject {

    @JsonProperty("dialogs")
    private ConversationsList mConversations;

    @JsonProperty("users")
    private List<User> mUsers;

    public ConversationsList getConversations() {
        return mConversations;
    }

    public void setConversations(ConversationsList conversations) {
        mConversations = conversations;
    }

    public List<User> getUsers() {
        return mUsers;
    }

    public void setUsers(List<User> users) {
        mUsers = users;
    }
}
