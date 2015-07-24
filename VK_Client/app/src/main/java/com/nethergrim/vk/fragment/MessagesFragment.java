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
import com.nethergrim.vk.event.ConversationsUpdatedEvent;
import com.nethergrim.vk.utils.SafeTimer;
import com.nethergrim.vk.views.RecyclerviewPageScroller;
import com.nethergrim.vk.web.DataManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class MessagesFragment extends AbstractFragment implements
        RecyclerviewPageScroller.OnRecyclerViewScrolledToPageListener,
        SwipeRefreshLayout.OnRefreshListener {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int UPDATE_DELAY_SEC = 60;
    @InjectView(R.id.list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.progressBar2)
    ProgressBar mProgressBar;
    @InjectView(R.id.textViewNothingHere)
    TextView mNothingHereTextView;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Inject
    DataManager mDataManager;

    @Inject
    Bus mBus;
    private SafeTimer mSafeTimer;
    private ConversationsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().getMainComponent().inject(this);
        mBus.register(this);
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
        mAdapter = new ConversationsAdapter();
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
        mSwipeRefreshLayout.setRefreshing(true);
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
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Override
    public void onRecyclerViewScrolledToPage(int pageNumber) {
        loadPage(pageNumber);
    }

    @Override
    public void onRefresh() {
        Log.e("TAG", "onRefresh");
        loadPage(0);
    }

    @Subscribe
    public void conversationsUpdated(ConversationsUpdatedEvent event) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() == 0) {
                mProgressBar.setVisibility(View.GONE);
                mNothingHereTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadPage(int pageNumber) {
        mDataManager.manageConversationsAndUsers(DEFAULT_PAGE_SIZE, pageNumber * DEFAULT_PAGE_SIZE,
                false);
    }

}
