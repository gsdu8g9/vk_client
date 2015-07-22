package com.nethergrim.vk.gcm;

import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.nethergrim.vk.MyApplication;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

// TODO do something here


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Log.e("TAG", "token refreshed");
        PushNotificationsRegisterIntentService.start(
                MyApplication.getInstance().getApplicationContext());
    }

}
