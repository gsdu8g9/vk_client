package com.nethergrim.vk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nethergrim.vk.R;
import com.nethergrim.vk.caching.models.Conversation;
import com.nethergrim.vk.web.WebResponseMapper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class MessagesFragment extends AbstractFragment {

    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mListView = (ListView) view.findViewById(R.id.list);
        loadData();
    }

    private void loadData() {
        VKRequest request = new VKRequest("messages.getDialogs");
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
