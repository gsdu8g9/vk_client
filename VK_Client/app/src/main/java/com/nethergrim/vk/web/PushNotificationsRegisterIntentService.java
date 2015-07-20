package com.nethergrim.vk.web;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.nethergrim.vk.MyApplication;

import javax.inject.Inject;

public class PushNotificationsRegisterIntentService extends IntentService {

    @Inject
    WebRequestManager mWebRequestManager;


    public PushNotificationsRegisterIntentService() {
        super("PushNotificationsRegisterIntentService");
    }

    public static void start(Context context) {
        Intent msgIntent = new Intent(context, PushNotificationsRegisterIntentService.class);
        context.startService(msgIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            registerForPushNotifications();
        }
    }

    private void registerForPushNotifications() {
        // TODO register for push notifications
    }

}
