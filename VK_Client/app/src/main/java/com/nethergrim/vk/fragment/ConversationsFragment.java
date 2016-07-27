package com.nethergrim.vk.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.activity.ChatActivity;
import com.nethergrim.vk.activity.NewChatActivity;
import com.nethergrim.vk.adapter.ConversationsAdapter;
import com.nethergrim.vk.callbacks.ToolbarScrollable;
import com.nethergrim.vk.event.ConversationsUpdatedEvent;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.utils.BasicRecyclerViewScroller;
import com.nethergrim.vk.utils.FabAnimationManager;
import com.nethergrim.vk.utils.RecyclerItemClickListener;
import com.nethergrim.vk.views.PaginationManager;
import com.nethergrim.vk.web.WebIntentHandler;
import com.rey.material.widget.ProgressView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * @author andreydrobyazko on 3/20/15.
 */
public class ConversationsFragment extends AbstractFragment
        implements PaginationManager.OnRecyclerViewScrolledToPageListener, ToolbarScrollable,
        RecyclerItemClickListener.OnItemClickListener, ConversationsAdapter.ClickListener {

    private static final int DEFAULT_PAGE_SIZE = 20;
    @InjectView(R.id.list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.progressBar2)
    ProgressBar mProgressBar;
    @InjectView(R.id.textViewNothingHere)
    TextView mNothingHereTextView;
    @Inject
    WebIntentHandler mWebIntentHandler;
    @Inject
    Bus mBus;
    @InjectView(R.id.fab_normal)
    FloatingActionButton mFabNormal;
    @InjectView(R.id.progressBottom)
    ProgressView mProgressBottom;
    private ConversationsAdapter mAdapter;

    private ToolbarScrollable mToolbarScrollable;
    private FabAnimationManager mFabAnimationManager;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ToolbarScrollable) {
            mToolbarScrollable = (ToolbarScrollable) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBus.register(this);
        View v = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFabAnimationManager = new FabAnimationManager(mFabNormal);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ConversationsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new PaginationManager(DEFAULT_PAGE_SIZE, this,
                DEFAULT_PAGE_SIZE / 2));
        mRecyclerView.addOnScrollListener(new BasicRecyclerViewScroller(this));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext(), this));
        if (mAdapter.getItemCount() == 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            mNothingHereTextView.setVisibility(View.GONE);
        }
        loadPage(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        mBus.unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarScrollable = null;
    }

    @Override
    public void onRecyclerViewScrolledToPage(int pageNumber) {
        loadPage(pageNumber - 1);
    }

    @Subscribe
    public void onDataUpdated(ConversationsUpdatedEvent event) {
        mProgressBottom.stop();
        mAdapter.setFooterVisibility(View.GONE);
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

    @Override
    public void showToolbar() {
        if (mFabNormal != null) {
            mFabAnimationManager.showFab();
        }
        if (mToolbarScrollable != null) {
            mToolbarScrollable.showToolbar();
        }
    }


    @Override
    public void hideToolbar() {
        if (mFabNormal != null) {
            mFabAnimationManager.hideFab();
        }
        if (mToolbarScrollable != null) {
            mToolbarScrollable.hideToolbar();
        }
    }

    @OnClick(R.id.fab_normal)
    public void onFabClicked(View v) {
        NewChatActivity.start(v.getContext());
    }


    @Override
    public void onConversationClicked(int index, Conversation conversation) {
        ChatActivity.start(getActivity(), conversation);
    }

    @Override
    public void onItemClick(View childView, int position) {

    }

    @Override
    public void onItemLongPress(View childView, int position) {
        Conversation conversation = mAdapter.getData(position);
        Context ctx = childView.getContext();
        // TODO replace with normal dialog
        MaterialDialog materialDialog = new MaterialDialog.Builder(ctx)
                .title(R.string.delete_chat_with)
                .content(R.string.are_you_sure)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .negativeColor(Color.BLACK)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mWebIntentHandler.deleteConversation(conversation);
                        dialog.dismiss();
                        super.onPositive(dialog);
                    }
                })
                .positiveColor(ctx.getResources().getColor(R.color.primary))
                .build();
        materialDialog.show();
    }

    private void loadPage(int pageNumber) {
        if (pageNumber == 0) {
            mProgressBottom.start();
        }
        mAdapter.setFooterVisibility(View.VISIBLE);
        mWebIntentHandler.fetchConversationsAndUsers(DEFAULT_PAGE_SIZE,
                pageNumber * DEFAULT_PAGE_SIZE, false);
    }
}
