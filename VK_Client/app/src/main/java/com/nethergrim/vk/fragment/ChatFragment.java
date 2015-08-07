package com.nethergrim.vk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.activity.AbstractActivity;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.ConversationUtils;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.views.InputMessagesController;
import com.nethergrim.vk.web.DataManager;
import com.nethergrim.vk.web.WebRequestManager;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;

/**
 * @author andrej on 07.08.15.
 */
public class ChatFragment extends AbstractFragment
        implements InputMessagesController.InputMessagesControllerInterface,
        Toolbar.OnMenuItemClickListener {

    public static final String EXTRA_CONVERSATION_ID = Constants.PACKAGE_NAME + ".CONV_ID";
    @Inject
    Realm mRealm;
    @Inject
    DataManager mDataManager;
    @Inject
    UserProvider mUserProvider;
    @Inject
    Prefs mPrefs;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @Inject
    WebRequestManager mWebRequestManager;
    @InjectView(R.id.btn_emoji)
    ImageButton mBtnEmoji;
    @InjectView(R.id.btn_send)
    ImageButton mBtnSend;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.inputMessagesController)
    InputMessagesController mInputMessagesController;
    private long mConversationId;
    private Conversation mConversation;
    private boolean mIsGroupChat;
    private User mAnotherUser;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadConversation();
        initToolbar();
        loadLastMessages();
        initViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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
    public void onMessageSent(String text) {
        // TODO handle
    }

    @Override
    public void onEmojiPressed() {
        // TODO handle
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.e("TAG", "menu item click " + item.getTitle());
        // TODO handle
        return false;
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

    private void initViews() {
        mInputMessagesController.setCallback(this);
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

    private void loadLastMessages() {
        mWebRequestManager.getChatHistory(0, 18, mIsGroupChat ? 0 : mConversationId,
                mIsGroupChat ? mConversationId : 0, 0, false, new WebCallback<ListOfMessages>() {
                    @Override
                    public void onResponseSucceed(ListOfMessages response) {
                        Log.e("TAG", "messages received: " + response.getMessages().size());
                    }

                    @Override
                    public void onResponseFailed(VKError e) {
                        Log.e("TAG", "error");
                    }
                });
    }
}
