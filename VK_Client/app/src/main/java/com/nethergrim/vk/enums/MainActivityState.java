package com.nethergrim.vk.enums;

import android.support.annotation.StringRes;

import com.nethergrim.vk.R;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public enum MainActivityState {
    Conversations(R.string.conversations, 0),
    Friends(R.string.friends, 1),
    Profile(R.string.profile, 2),
    Settings(R.string.settings, 4),
    Photos(R.string.photos, 3);

    private int mTitleString;
    private int mId;

    MainActivityState(@StringRes int title, int id) {
        mTitleString = title;
        mId = id;
    }

    public int getTitleStringRes() {
        return mTitleString;
    }

    public int getId() {
        return mId;
    }

    public static MainActivityState getStateForId(int id) {
        for (MainActivityState mainActivityState : MainActivityState.values()) {
            if (mainActivityState.getId() == id) {
                return mainActivityState;
            }
        }
        return Conversations;
    }
}
