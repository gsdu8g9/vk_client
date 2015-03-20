package com.nethergrim.vk.activity;

import android.os.Bundle;
import android.util.Log;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.R;
import com.vk.sdk.VKSdk;


public class StartActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "authorize");
        if (VKSdk.wakeUpSession(this)) {
            Log.e("TAG", "wake up session - true");
        } else {
            VKSdk.authorize(Constants.PERMISSIONS, true, false);
        }
    }
}
