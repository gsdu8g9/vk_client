package com.nethergrim.vk.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.web.DataManager;
import com.nethergrim.vk.web.WebIntentHandler;

import javax.inject.Inject;

/**
 * Created on 01.08.16.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    @Inject
    WebIntentHandler handler;

    @Inject
    DataManager dataManager;

    public ConnectionReceiver() {
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isOnline(context)) {
            handler.syncUnreadMessages();
            dataManager.syncPendingMessages();
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnectedOrConnecting());

    }
}
