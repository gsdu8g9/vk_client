package com.nethergrim.vk.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.vk.sdk.VKUIHelper;

/**
 * Activity that should be superclass in every activity in the app.
 * Contains some basic functionality for all screens.
 *
 * @author andreydrobyazko on 3/20/15. (c2q9450@gmail.com).
 *         All rights reserved.
 */
public abstract class AbstractActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this); // FIXME: 22.08.15 remove
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data); // FIXME: 22.08.15 remove
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);// FIXME: 22.08.15 remove fucking VK UI Helper, that leads to
        // memory leak.

    }

    protected void showFragment(Fragment fragment,
            boolean addToBackStack,
            boolean animate,
            int containerId) {
        String tag = fragment.getClass().getSimpleName();
//        Fragment alreadyExistingFragment = getFragmentManager().findFragmentByTag(tag);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        if (alreadyExistingFragment != null) {
//            transaction.show(alreadyExistingFragment);
//        } else {
//            transaction.add(containerId, fragment, tag);
//        }
        transaction.replace(containerId, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        if (animate) {
            // TODO implement fragment animations API
        }
        transaction.commit();
    }
}
