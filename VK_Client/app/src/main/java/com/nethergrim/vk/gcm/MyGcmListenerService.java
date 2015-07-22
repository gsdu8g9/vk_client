package com.nethergrim.vk.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class MyGcmListenerService extends GcmListenerService {


    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        // TODO handle messages receiving
    }
}
