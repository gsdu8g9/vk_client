package com.nethergrim.vk.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.nethergrim.vk.web.WebRequestManager;

import javax.inject.Inject;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class MyGcmListenerService extends GcmListenerService {


    @Inject
    WebRequestManager mWebRequestManager;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String message = data.getString("message");
        Log.e("TAG", "From: " + from);
        Log.e("TAG", "Message: " + message);
        // TODO handle messages receiving

    }
}
