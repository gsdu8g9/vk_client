package com.nethergrim.vk.caching;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nethergrim.vk.MyApplication;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class PrefsImpl implements Prefs {

    private static final String KEY_USER_ID = "id";
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

}
