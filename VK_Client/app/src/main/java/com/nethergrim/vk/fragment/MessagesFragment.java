package com.nethergrim.vk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.ConversationsAdapter;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.views.DividerItemDecoration;
import com.nethergrim.vk.views.RecyclerviewPageScroller;
import com.nethergrim.vk.web.WebRequestManager;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class MessagesFragment extends AbstractFragment implements WebCallback<ConversationsList>, RecyclerviewPageScroller.OnRecyclerViewScrolledToPageListener {

    public static final int DEFAULT_PAGE_SIZE = 20;
    @InjectView(R.id.list)
    RecyclerView mRecyclerView;
    @Inject
    WebRequestManager mWM;
    private ConversationsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (mAdapter != null) {
            mAdapter.closeRealm();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setHasFixedSize(true);
        if (checkRealm()) {
            realm.setAutoRefresh(true);
            RealmResults<Conversation> data = realm.where(Conversation.class).findAllSorted("date", false);
            mAdapter = new ConversationsAdapter(data);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            mRecyclerView.setOnScrollListener(new RecyclerviewPageScroller(DEFAULT_PAGE_SIZE, this, 5));
        }
        loadPage(0);
    }

    private void loadPage(int pageNumber) {
        Log.e("TAG", "loading page: " + pageNumber);
        mWM.getConversations(DEFAULT_PAGE_SIZE, pageNumber * DEFAULT_PAGE_SIZE, false, 0, this);
    }

    @Override
    public void onResponseSucceed(ConversationsList response) {
        if (response != null && checkRealm()) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(response.getResults());
            realm.commitTransaction();
            mWM.getUsersForConversations(response, new WebCallback<ListOfUsers>() {
                @Override
                public void onResponseSucceed(ListOfUsers response) {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.getResponse());
                    realm.commitTransaction();
                }

                @Override
                public void onResponseFailed(VKError e) {
                    Log.e("TAG", "error: " + e.errorMessage);
                    // TODO handle
                }
            });
        }
    }

    @Override
    public void onResponseFailed(VKError e) {
        Log.e("TAG", "e: " + e.errorMessage);
        // TODO handle
    }


    @Override
    public void onRecyclerViewScrolledToPage(int pageNumber) {
        loadPage(pageNumber);
    }
}
