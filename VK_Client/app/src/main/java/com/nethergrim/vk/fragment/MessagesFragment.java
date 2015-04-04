package com.nethergrim.vk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nethergrim.vk.R;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.web.WebRequestManager;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class MessagesFragment extends AbstractFragment implements WebCallback<ConversationsList> {

    @InjectView(R.id.list)
    ListView mListView;
    @Inject
    WebRequestManager mWM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        mWM.getConversations(20, 0, false, 0, this);
    }

    @Override
    public void onResponseSucceed(ConversationsList response) {
        if (response != null && checkRealm()){
            long start = System.currentTimeMillis();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(response.getResults());
            realm.commitTransaction();
            Log.e("TAG","saved " + response.getResults().size() + " Conversations to Realm in: " + String.valueOf(System.currentTimeMillis() - start));
        }
    }

    @Override
    public void onResponseFailed(VKError e) {
        Log.e("TAG","e: " + e.errorMessage);
    }
}
