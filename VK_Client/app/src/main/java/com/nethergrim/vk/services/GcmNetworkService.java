package com.nethergrim.vk.services;

import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.web.DataManager;

import javax.inject.Inject;

/**
 * Created on 28.07.16.
 */

public class GcmNetworkService extends GcmTaskService {

    private static final String TAG = "GcmNetworkService";

    @Inject
    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    /**
     * Trigger marking messages as read.
     *
     * @return 0
     */
    @Override
    public int onRunTask(TaskParams taskParams) {
        try {
            dataManager.syncMessagesReadState().toBlocking().last();
        } catch (Throwable e) {
            Log.e(TAG, "onRunTask: ", e);
        }
        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
