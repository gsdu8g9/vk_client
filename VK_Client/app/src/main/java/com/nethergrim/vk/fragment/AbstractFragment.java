package com.nethergrim.vk.fragment;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import io.realm.Realm;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public abstract class AbstractFragment extends Fragment {


    protected Realm mRealm;

    @Override
    public void onStart() {
        super.onStart();
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
        mRealm.close();
    }

    protected void showToast(String s) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
    }

    protected void showToast(@StringRes int s) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

}
