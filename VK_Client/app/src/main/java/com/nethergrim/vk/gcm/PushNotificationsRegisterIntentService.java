package com.nethergrim.vk.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.web.WebRequestManager;

import java.io.IOException;

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
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(Constants.GCM_SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.e("TAG", "GCM token: " + token);
            mWebRequestManager.registerToPushNotifications(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
