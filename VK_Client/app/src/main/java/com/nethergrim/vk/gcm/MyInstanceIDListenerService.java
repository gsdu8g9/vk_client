package com.nethergrim.vk.gcm;

import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.web.DataManager;

import javax.inject.Inject;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Inject
    Prefs mPrefs;

    @Inject
    DataManager mDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Log.e("TAG", "token refreshed");
        mPrefs.setGcmToken(null);
        mDataManager.launchStartupTasks();
    }

}
