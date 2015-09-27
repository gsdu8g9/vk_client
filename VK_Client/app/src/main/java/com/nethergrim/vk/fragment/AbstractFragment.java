package com.nethergrim.vk.fragment;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public abstract class AbstractFragment extends Fragment {

    protected void showToast(String s) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(@StringRes int s) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

}
