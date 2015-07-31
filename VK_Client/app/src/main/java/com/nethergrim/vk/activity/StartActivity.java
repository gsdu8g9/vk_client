package com.nethergrim.vk.activity;

import android.content.Intent;
import android.os.Bundle;

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
        checkSession();
    }

    private void checkSession() {
        if (VKSdk.wakeUpSession(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            VKSdk.authorize(Constants.PERMISSIONS);
        }
    }
}
