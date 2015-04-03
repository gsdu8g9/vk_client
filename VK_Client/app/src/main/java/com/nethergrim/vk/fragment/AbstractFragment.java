package com.nethergrim.vk.fragment;

import android.app.Activity;
import android.app.Fragment;

import com.nethergrim.vk.activity.AbstractActivity;

import io.realm.Realm;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public abstract class AbstractFragment extends Fragment {

    protected Realm getRealm(){
        Activity activity = getActivity();
        if (activity != null && activity instanceof AbstractActivity){
            AbstractActivity abstractActivity = (AbstractActivity) activity;
            return abstractActivity.getRealm();
        }
        return null;
    }
}
