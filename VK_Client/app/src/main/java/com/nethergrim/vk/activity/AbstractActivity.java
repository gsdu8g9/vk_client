package com.nethergrim.vk.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.vk.sdk.VKUIHelper;

import io.realm.Realm;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public abstract class AbstractActivity extends ActionBarActivity {

    public Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null){
            realm.close();
        }
        VKUIHelper.onDestroy(this);
    }

    public Realm getRealm() {
        return realm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    protected void showFragment(Fragment fragment, boolean addToBackStack, boolean animate, int containerId) {
        String tag = fragment.getClass().getSimpleName();
        Fragment alreadyExistingFragment = getFragmentManager().findFragmentByTag(tag);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (alreadyExistingFragment != null) {
            transaction.show(alreadyExistingFragment);
        } else {
            transaction.add(containerId, fragment, tag);
        }
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        if (animate) {
            // TODO
        }
        transaction.commit();
    }
}
