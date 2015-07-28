package com.nethergrim.vk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.FriendsAdapter;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.event.UsersUpdatedEvent;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.views.VarColumnGridLayoutManager;
import com.nethergrim.vk.web.DataManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andrej on 28.07.15.
 */
public class FriendsFragment extends AbstractFragment
        implements WebCallback<ListOfUsers> {

    @InjectView(R.id.progressBar2)
    ProgressBar mProgressBar2;
    @InjectView(R.id.textViewNothingHere)
    TextView mTextViewNothingHere;
    @InjectView(R.id.list)
    RecyclerView mList;


    @Inject
    DataManager mDataManager;
    @Inject
    Bus mBus;

    private FriendsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.inject(this, view);
        MyApplication.getInstance().getMainComponent().inject(this);
        mBus.register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view.getContext());
        updateFriendsFromBackend();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        mBus.unregister(this);
    }

    @Subscribe
    public void onDataChanged(UsersUpdatedEvent e) {
        if (mProgressBar2 == null) {
            return;
        }
        if (mAdapter.getItemCount() != 0) {
            mProgressBar2.setVisibility(View.GONE);
            mTextViewNothingHere.setVisibility(View.GONE);
        } else {
            mTextViewNothingHere.setVisibility(View.VISIBLE);
            mProgressBar2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResponseSucceed(ListOfUsers response) {
        onDataChanged(new UsersUpdatedEvent());
    }

    @Override
    public void onResponseFailed(VKError e) {
        Log.e("TAG", "error: " + e.toString());
    }

    private void updateFriendsFromBackend() {
        mDataManager.manageFriends(this);
    }

    private void initList(final Context context) {
        mAdapter = new FriendsAdapter();
        mList.setAdapter(mAdapter);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new VarColumnGridLayoutManager(context, context.getResources()
                .getDimensionPixelSize(R.dimen.friends_screen_min_item_width)));
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getItemCount() > 0) {
            mProgressBar2.setVisibility(View.GONE);
            mTextViewNothingHere.setVisibility(View.GONE);
        }
    }

}
