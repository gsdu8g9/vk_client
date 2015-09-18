package com.nethergrim.vk.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.activity.AbstractActivity;
import com.nethergrim.vk.adapter.ChatAdapter;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.event.ConversationUpdatedEvent;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.ConversationUtils;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.views.PaginationManager;
import com.nethergrim.vk.web.WebIntentHandler;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;

/**
 * @author andrej on 07.08.15.
 */
public class ChatFragment extends AbstractFragment
        implements Toolbar.OnMenuItemClickListener,
        PaginationManager.OnRecyclerViewScrolledToPageListener {

    public static final String EXTRA_CONVERSATION_ID = Constants.PACKAGE_NAME + ".CONV_ID";
    public static final int PAGE_SIZE = 5;
    public static final int PAGE_OFFSET_PRELOAD = 0;
    @Inject
    WebIntentHandler mWebIntentHandler;
    @Inject
    UserProvider mUserProvider;
    @Inject
    Prefs mPrefs;
    @Inject
    Bus mBus;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.btn_emoji)
    ImageButton mBtnEmoji;
    @InjectView(R.id.btn_send)
    ImageButton mBtnSend;
    @InjectView(R.id.inputMessagesController)
    RelativeLayout mInputMessagesController;
    Realm mRealm;
    private long mConversationId;
    private boolean mIsGroupChat;
    private Conversation mConversation;
    private User mAnotherUser;
    private ChatAdapter mChatAdapter;
    private long mDataCount;
    private boolean mLoading;

    public static ChatFragment getInstance(long conversationId, boolean isAGroupChat) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_CONVERSATION_ID, conversationId);
        args.putBoolean(Constants.EXTRA_GROUP_CONVERSATION, isAGroupChat);
        chatFragment.setArguments(args);
        return chatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConversationId = getConversationIdFromExtras(savedInstanceState);
        MyApplication.getInstance().getMainComponent().inject(this);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mRealm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        mBus.register(this);
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view.getContext());
        loadConversation();
        initToolbar();
        loadLastMessages();
    }

    @Override
    public void onDestroyView() {
        mBus.unregister(this);
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_chat, menu);
        if (mIsGroupChat) {
            menu.removeItem(R.id.action_user_profile);
        } else {
            menu.removeItem(R.id.action_group_conversation_details);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.e("TAG", "menu item click " + item.getTitle());
        // TODO handle
        return false;
    }

    @Subscribe
    public void onDataUpdated(ConversationUpdatedEvent e) {
        if (mIsGroupChat && e.getChatId() == getChatId() || !mIsGroupChat && e.getUserId()
                .equals(getUserId())) {
            mLoading = false;
            mDataCount = e.getCount();
            mChatAdapter.notifyDataSetChanged();
            mChatAdapter.setHeaderVisibility(View.GONE);
        }
    }

    @Override
    public void onRecyclerViewScrolledToPage(int pageNumber) {
        if (!mLoading && mChatAdapter.getDataSize() != mDataCount) {
            mLoading = true;
            mChatAdapter.setHeaderVisibility(View.VISIBLE);
            int offset = pageNumber * PAGE_SIZE;
            Log.e("TAG", "loading data, offset: " + offset);
            mWebIntentHandler.fetchMessagesHistory(PAGE_SIZE, offset, getUserId(), getChatId());
        }
    }

    private long getConversationIdFromExtras(Bundle extras) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(EXTRA_CONVERSATION_ID)) {
            mIsGroupChat = args.getBoolean(Constants.EXTRA_GROUP_CONVERSATION, false);
            return args.getLong(EXTRA_CONVERSATION_ID);
        } else if (extras != null && extras.containsKey(EXTRA_CONVERSATION_ID)) {
            return extras.getLong(EXTRA_CONVERSATION_ID);
        } else
            return 0;
    }

    private void initList(Context context) {
        mChatAdapter = new ChatAdapter(mConversationId, mIsGroupChat);
        mRecyclerView.setAdapter(mChatAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        llm.setStackFromEnd(true);
        mRecyclerView.addOnScrollListener(
                new PaginationManager(PAGE_SIZE, this, PAGE_OFFSET_PRELOAD, true));
        mRecyclerView.setLayoutManager(llm);
    }

    private void loadConversation() {
        mConversation = mRealm.where(Conversation.class).equalTo("id", mConversationId).findFirst();
        if (mConversation == null) {
            return;
        }
        mIsGroupChat = ConversationUtils.isConversationAGroupChat(mConversation);
        if (!mIsGroupChat) {
            mAnotherUser = mUserProvider.getUser(mConversation.getId());
        }
    }

    private String getToolbarTitle() {
        if (mConversation == null) {
            return null;
        }
        if (ConversationUtils.isConversationAGroupChat(mConversation)) {
            return mConversation.getMessage().getTitle();
        } else {
            return mAnotherUser.getFirstName() + " " + mAnotherUser.getLastName();
        }
    }

    private void initToolbar() {
        ((AbstractActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setOnMenuItemClickListener(this);
        ((AbstractActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        (getActivity()).setTitle(getToolbarTitle());

    }

    private String getUserId() {
        if (mIsGroupChat) {
            return null;
        }
        return String.valueOf(mConversationId);
    }

    private long getChatId() {
        if (!mIsGroupChat) {
            return -1;
        }
        return mConversationId;
    }

    private void loadLastMessages() {
        mWebIntentHandler.fetchMessagesHistory(PAGE_SIZE, 0, getUserId(), getChatId());
    }
}
