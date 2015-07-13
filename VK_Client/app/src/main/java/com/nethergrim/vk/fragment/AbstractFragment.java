package com.nethergrim.vk.fragment;

import android.app.Activity;
import android.app.Fragment;
import io.realm.Realm;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public abstract class AbstractFragment extends Fragment {


    protected Realm realm;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        realm = null;
    }

    protected boolean checkRealm(){
        return realm != null;
    }

}
