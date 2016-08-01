package com.nethergrim.vk.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 3/20/15.
 */
public abstract class AbstractFragment extends Fragment implements RealmChangeListener {


    protected Realm mRealm;

    @Override
    public void onCreate(Bundle b) {
        super.onStart();
        mRealm = Realm.getDefaultInstance();
        mRealm.setAutoRefresh(true);
        mRealm.addChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onStop();
        mRealm.removeChangeListener(this);
        mRealm.close();
        mRealm = null;
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

    @Override
    public void onChange(Object element) {

    }
}
