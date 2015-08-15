package com.nethergrim.vk.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.web.WebRequestManager;

import java.io.IOException;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PushNotificationsRegisterService extends Service {

    @Inject
    WebRequestManager mWebRequestManager;

    @Inject
    Prefs mPrefs;


    public static void start(Context context) {
        Intent msgIntent = new Intent(context, PushNotificationsRegisterService.class);
        context.startService(msgIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            registerForPushNotifications();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void registerForPushNotifications() {

        Observable
                .just(Boolean.TRUE)
                .observeOn(Schedulers.newThread())
                .subscribe(
                        new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                String token = mPrefs.getGcmToken();
                                if (TextUtils.isEmpty(token)) {
                                    InstanceID instanceID = InstanceID.getInstance(
                                            MyApplication.getInstance().getApplicationContext());
                                    try {
                                        token = instanceID.getToken(Constants.GCM_SENDER_ID,
                                                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                                    } catch (IOException e) {
//                                        e.printStackTrace();
                                    }
                                    mPrefs.setGcmToken(token);
                                }
                                Log.e("TAG", "registering token: " + token);
                                mWebRequestManager.registerToPushNotifications(token);
                            }
                        });

    }

}
