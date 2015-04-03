package com.nethergrim.vk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.R;
import com.nethergrim.vk.caching.models.Conversation;
import com.nethergrim.vk.modules.Injector;
import com.nethergrim.vk.web.WebRequestManager;
import com.nethergrim.vk.web.WebResponseMapper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class MessagesFragment extends AbstractFragment {

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
        Injector.getInstance().inject(this);
        if (mWM != null){
            Log.e("TAG", "mwrm != null");
            List<Conversation> conversations = mWM.getConversations(0,0,false,0);
            if (conversations != null){
                Log.e("TAG", "conversations != null size: " + conversations.size());
            }
        } else {
            Log.e("TAG", "MWRM NULL");
        }
        VKRequest request = new VKRequest(Constants.Requests.MESSAGES_GET_DIALOGS);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.e("TAG", "complete");

                showData(WebResponseMapper.getConversations(response));
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
                Log.e("TAG", "attempt failed");
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.e("TAG", "error");
            }

        });
    }

    private void showData(List<Conversation> conversations) {
        List<String> messages = new ArrayList<>();
        for (Conversation conversation : conversations) {
            messages.add(conversation.getLastMessage().getBody());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, messages);
        mListView.setAdapter(adapter);
    }
}
