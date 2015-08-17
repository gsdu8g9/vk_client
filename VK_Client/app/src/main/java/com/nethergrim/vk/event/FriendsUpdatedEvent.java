package com.nethergrim.vk.event;

/**
 * @author andrej on 15.08.15.
 */
public class FriendsUpdatedEvent extends UsersUpdatedEvent {

    private int mFriendsCount;

    public FriendsUpdatedEvent(int friendsCount) {
        mFriendsCount = friendsCount;
    }

    public int getFriendsCount() {
        return mFriendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        mFriendsCount = friendsCount;
    }
}
