package com.nethergrim.vk.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.ConversationsAdapter;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.utils.SafeTimer;
import com.nethergrim.vk.views.RecyclerviewPageScroller;
import com.nethergrim.vk.web.WebRequestManager;
import com.vk.sdk.api.VKError;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class MessagesFragment extends AbstractFragment implements WebCallback<ConversationsList>,
        RecyclerviewPageScroller.OnRecyclerViewScrolledToPageListener,
        SwipeRefreshLayout.OnRefreshListener {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int UPDATE_DELAY_SEC = 15;
    @InjectView(R.id.list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.progressBar2)
    ProgressBar mProgressBar;
    @InjectView(R.id.textViewNothingHere)
    TextView mNothingHereTextView;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Inject
    WebRequestManager mWM;
    private SafeTimer mSafeTimer;
    private ConversationsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().getMainComponent().inject(this);
        mSafeTimer = new SafeTimer(new Runnable() {
            @Override
            public void run() {
                loadPage(0);
            }
        }, UPDATE_DELAY_SEC);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setHasFixedSize(true);
        if (checkRealm()) {
            realm.setAutoRefresh(true);
            RealmResults<Conversation> data = realm.where(Conversation.class)
                    .findAllSorted("date", false);
            mAdapter = new ConversationsAdapter(data);
            mRecyclerView.setAdapter(mAdapter);
            Resources res = view.getResources();
            int additionalLeftMarginForDividers =
                    2 * (res.getDimensionPixelSize(R.dimen.conversation_item_padding_horizontal))
                            + res.getDimensionPixelSize(R.dimen.conversation_avatar_size);
            mRecyclerView.addItemDecoration(
                    new HorizontalDividerItemDecoration.Builder(view.getContext()).margin(
                            additionalLeftMarginForDividers, 0).build());
            mRecyclerView.setOnScrollListener(
                    new RecyclerviewPageScroller(DEFAULT_PAGE_SIZE, this, 5));
            if (mAdapter.getItemCount() == 0) {
                mProgressBar.setVisibility(View.VISIBLE);
                mNothingHereTextView.setVisibility(View.GONE);
            }
        }
        loadPage(0);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSafeTimer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSafeTimer.finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRecyclerViewScrolledToPage(int pageNumber) {
        loadPage(pageNumber);
    }

    @Override
    public void onRefresh() {
        loadPage(0);
    }

    @Override
    public void onResponseSucceed(ConversationsList response) {
        if (response != null && checkRealm()) {
            mSwipeRefreshLayout.setRefreshing(false);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(response.getResults());
            realm.commitTransaction();
            if (response.getResults().isEmpty() && mAdapter != null
                    && mAdapter.getItemCount() == 0) {
                mProgressBar.setVisibility(View.GONE);
                mNothingHereTextView.setVisibility(View.VISIBLE);
            }
            mWM.getUsersForConversations(response, new WebCallback<ListOfUsers>() {
                @Override
                public void onResponseSucceed(ListOfUsers response) {
                    if (checkRealm()) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(response.getResponse());
                        realm.commitTransaction();
                        mAdapter.notifyDataSetChanged();
                        mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onResponseFailed(VKError e) {
                    Log.e("TAG", "error: " + e.errorMessage);
                    // TODO handle
                }
            });
        }
    }

    private void loadPage(int pageNumber) {
        mWM.getConversations(DEFAULT_PAGE_SIZE, pageNumber * DEFAULT_PAGE_SIZE, false, 0, this);
    }

    @Override
    public void onResponseFailed(VKError e) {
        Log.e("TAG", "e: " + e.errorMessage);
        // TODO handle
    }

}
