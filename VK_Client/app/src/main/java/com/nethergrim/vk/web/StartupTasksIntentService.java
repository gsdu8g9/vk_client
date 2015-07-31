package com.nethergrim.vk.web;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.gcm.PushNotificationsRegisterService;
import com.nethergrim.vk.models.ListOfUsers;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;


public class StartupTasksIntentService extends Service {

    public static final String TAG = StartupTasksIntentService.class.getName();
    private static final String ACTION_LAUNCH_STARTUP_TASKS
            = "com.nethergrim.vk.web.action.LAUNCH_STARTUP_TASKS";
    @Inject
    DataManager mDataManager;
    @Inject
    WebRequestManager mWebRequestManager;

    /**
     * Startup tasks to be performed by default:
     * connect to GCM
     * become online
     * fetch friends
     * fetch last messages
     */
    public static void startActionLaunchStartupTasks(Context context) {
        Intent intent = new Intent(context, StartupTasksIntentService.class);
        intent.setAction(ACTION_LAUNCH_STARTUP_TASKS);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyApplication.getInstance().getMainComponent().inject(this);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LAUNCH_STARTUP_TASKS.equals(action)) {
                handleActionLaunchStartupTasks();
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleActionLaunchStartupTasks() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PushNotificationsRegisterService.start(StartupTasksIntentService.this);
                mWebRequestManager.registerOnline();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mDataManager.manageConversationsAndUsers(10, 0, false);

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mDataManager.manageFriends(new WebCallback<ListOfUsers>() {
                    @Override
                    public void onResponseSucceed(ListOfUsers response) {
                        Log.d(TAG, "fetched friends successfully");
                    }

                    @Override
                    public void onResponseFailed(VKError e) {
                        Log.e(TAG, "fetching friends error: " + e.toString());
                    }
                });
            }
        }).start();
    }

}
