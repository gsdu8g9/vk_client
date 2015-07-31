package com.nethergrim.vk.web;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.ListOfUsers;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;


public class StartupTasksIntentService extends IntentService {

    public static final String TAG = StartupTasksIntentService.class.getName();
    private static final String ACTION_LAUNCH_STARTUP_TASKS
            = "com.nethergrim.vk.web.action.LAUNCH_STARTUP_TASKS";
    @Inject
    DataManager mDataManager;
    @Inject
    WebRequestManager mWebRequestManager;

    public StartupTasksIntentService() {
        super("StartupTasksIntentService");
    }

    /**
     * Startup tasks to be permormed by default:
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
    protected void onHandleIntent(Intent intent) {
        MyApplication.getInstance().getMainComponent().inject(this);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LAUNCH_STARTUP_TASKS.equals(action)) {
                handleActionLaunchStartupTasks();
            }
        }
    }

    private void handleActionLaunchStartupTasks() {
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

        mDataManager.manageConversationsAndUsers(10, 0, false);
    }

}
