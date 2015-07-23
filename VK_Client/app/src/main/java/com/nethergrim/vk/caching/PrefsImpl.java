package com.nethergrim.vk.caching;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.enums.MainActivityState;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class PrefsImpl implements Prefs {

    public static final String KEY_USER_ID = "id";
    public static final String KEY_ACTIVITY_STATE_ID = "activity_state_id";
    public static final String KEY_GCM_TOKEN = "gcm_token";
    private static final String KEY_TOKEN = "token";
    private SharedPreferences mPrefs;

    public PrefsImpl() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(
                MyApplication.getInstance().getApplicationContext());
    }

    @Override
    public long getCurrentUserId() {
        return mPrefs.getLong(KEY_USER_ID, 0);
    }

    @Override
    public void setCurrentUserId(long userId) {
        mPrefs.edit().putLong(KEY_USER_ID, userId).apply();
    }

    @Override
    public String getToken() {
        return mPrefs.getString(KEY_TOKEN, null);
    }

    @Override
    public void setToken(String token) {
        mPrefs.edit().putString(KEY_TOKEN, token).apply();
    }

    @Override
    public int getCurrentActivityStateId() {
        return mPrefs.getInt(KEY_ACTIVITY_STATE_ID, MainActivityState.Conversations.getId());
    }

    @Override
    public void setCurrentActivityStateId(int id) {
        mPrefs.edit().putInt(KEY_ACTIVITY_STATE_ID, id).apply();
    }

    @Override
    public void setGcmToken(String token) {
        mPrefs.edit().putString(KEY_GCM_TOKEN, token).apply();
    }


    @Override
    public String getGcmToken() {
        return mPrefs.getString(KEY_GCM_TOKEN, null);
    }

}
