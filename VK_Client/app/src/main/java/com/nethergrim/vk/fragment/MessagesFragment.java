package com.nethergrim.vk.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.ConversationsAdapter;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.views.RecyclerviewPageScroller;
import com.nethergrim.vk.web.WebRequestManager;
import com.vk.sdk.api.VKError;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import io.realm.RealmResults;

import javax.inject.Inject;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class MessagesFragment extends AbstractFragment implements WebCallback<ConversationsList>, RecyclerviewPageScroller.OnRecyclerViewScrolledToPageListener {

    public static final int DEFAULT_PAGE_SIZE = 20;
    @InjectView(R.id.list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.progressBar2)
    ProgressBar mProgressBar;
    @InjectView(R.id.textViewNothingHere)
    TextView mNothingHereTextView;

    @Inject
    WebRequestManager mWM;
    private ConversationsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().getMainComponent().inject(this);
    }

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
            Resources res = view.getResources();
            int additionalLeftMarginForDividers = 2 * (res.getDimensionPixelSize(R.dimen.conversation_item_padding_horizontal)) + res.getDimensionPixelSize(R.dimen.conversation_avatar_size);
            mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(view.getContext()).margin(additionalLeftMarginForDividers, 0).build());
            mRecyclerView.setOnScrollListener(new RecyclerviewPageScroller(DEFAULT_PAGE_SIZE, this, 5));
            if (mAdapter.getItemCount() == 0) {
                mProgressBar.setVisibility(View.VISIBLE);
                mNothingHereTextView.setVisibility(View.GONE);
            }
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
            if (response.getResults().isEmpty() && mAdapter != null && mAdapter.getItemCount() == 0) {
                mProgressBar.setVisibility(View.GONE);
                mNothingHereTextView.setVisibility(View.VISIBLE);
            }
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
