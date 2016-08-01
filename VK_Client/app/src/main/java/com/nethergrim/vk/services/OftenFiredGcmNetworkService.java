package com.nethergrim.vk.services;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.web.DataManager;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

/**
 * Created on 29.07.16.
 */

public class OftenFiredGcmNetworkService extends GcmTaskService {


    @Inject
    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    @DebugLog
    public int onRunTask(TaskParams taskParams) {
        dataManager.syncPendingMessages().toBlocking().last();
        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
