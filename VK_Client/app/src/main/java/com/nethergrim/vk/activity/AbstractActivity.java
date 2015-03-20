package com.nethergrim.vk.activity;

import android.app.Activity;
import android.content.Intent;

import com.vk.sdk.VKUIHelper;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public abstract class AbstractActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }
}
