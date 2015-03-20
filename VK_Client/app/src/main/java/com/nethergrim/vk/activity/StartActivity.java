package com.nethergrim.vk.activity;

import android.content.Intent;
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

    private void checkSession() {
        if (VKSdk.wakeUpSession(this)) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Log.e("TAG", "authorizing");
            VKSdk.authorize(Constants.PERMISSIONS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSession();
    }
}
