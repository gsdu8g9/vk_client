package com.nethergrim.vk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.ChatAdapter;
import com.nethergrim.vk.adapter.SelectableUltimateAdapter;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.event.ConversationUpdatedEvent;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.Message;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.models.outcoming_attachments.MessageAttachment;
import com.nethergrim.vk.utils.ConversationUtils;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.views.PaginationManager;
import com.nethergrim.vk.web.WebIntentHandler;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author andrej on 07.08.15.
 */
public class ChatFragment extends BaseKeyboardFragment implements Toolbar.OnMenuItemClickListener,
        PaginationManager.OnRecyclerViewScrolledToPageListener {

    private static final String EXTRA_CONVERSATION_ID = Constants.PACKAGE_NAME + ".CONV_ID";
    private static final int PAGE_SIZE = 50;
    @Inject
    WebIntentHandler mWebIntentHandler;
    @Inject
    UserProvider mUserProvider;
    @Inject
    Prefs mPrefs;
    @Inject
    Bus mBus;


    private long mConversationId;
    private boolean mIsGroupChat;
    private Conversation mConversation;
    private User mAnotherUser;
    private ChatAdapter mChatAdapter;
    private long mDataCount;

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
    public void onDestroyView() {
        mBus.unregister(this);
        super.onDestroyView();
    }

    @Override
    public void initRecyclerView(RecyclerView recycler) {
        LinearLayoutManager llm = new LinearLayoutManager(recycler.getContext(),
                RecyclerView.VERTICAL, true);
        recycler.addOnScrollListener(new PaginationManager(PAGE_SIZE, this, true));
        recycler.setLayoutManager(llm);
        loadLastMessages();
        mBus.register(this);
    }

    @Override
    public void postText(String text) {
        // TODO: 03.10.15 implement
    }

    @Override
    public String getTitle() {
        if (mConversation == null) {
            return null;
        }
        if (ConversationUtils.isConversationAGroupChat(mConversation)) {
            return mConversation.getMessage().getTitle();
        } else {
            return mAnotherUser.getFirstName() + " " + mAnotherUser.getLastName();
        }
    }

    @Override
    public void deleteSelectedMessages(Set<Long> dataIds) {
        // TODO: 17.10.15 implement
    }

    @Override
    public List<MessageAttachment> getSelectedMessages() {
        // TODO: 17.10.15 implement
        return null;
    }

    @Override
    public String getSelectedText() {
        return "TODO"; // TODO: 17.10.15 fixme
    }

    @Override
    protected SelectableUltimateAdapter getAdapter(Context context) {
        mRealm = Realm.getDefaultInstance();
        mConversation = mRealm.where(Conversation.class).equalTo("id", mConversationId).findFirst();
        if (mConversation == null) {
            return null;
        }
        mIsGroupChat = ConversationUtils.isConversationAGroupChat(mConversation);
        if (!mIsGroupChat) {
            mAnotherUser = mUserProvider.getUser(mConversation.getId());
        }

        RealmResults<Message> mMessages;
        if (mIsGroupChat) {
            mMessages = mRealm.where(Message.class)
                    .equalTo("chat_id", mConversationId)
                    .findAllSorted("date", Sort.DESCENDING);
        } else {
            mMessages = mRealm.where(Message.class)
                    .equalTo("user_id", mConversationId)
                    .findAllSorted("date", Sort.DESCENDING);
        }
        mChatAdapter = new ChatAdapter(mMessages);
        markMessagesAsRead();
        return mChatAdapter;
    }

    @Override
    public void initToolbar(Toolbar toolbar) {
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.e("TAG", "menu item click " + item.getTitle());
        // TODO handle
        return false;
    }

    private void markMessagesAsRead(){
        long convId;
        if (mIsGroupChat){
            convId = getChatId() + 2000000000;
        } else {
            convId = mConversationId;
        }
        mWebIntentHandler.markMessagesAsRead(convId, mChatAdapter.getLastMessageId());
    }

    @Subscribe
    public void onDataUpdated(ConversationUpdatedEvent e) {
        if (mIsGroupChat && e.getChatId() == getChatId() || !mIsGroupChat && e.getUserId().equals(
                getUserId())) {
            mDataCount = e.getCount();
            mChatAdapter.notifyDataSetChanged();
            mChatAdapter.setHeaderVisibility(View.GONE);
            markMessagesAsRead();
        }
    }

    @Override
    public void onChange() {
        super.onChange();
        mChatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecyclerViewScrolledToPage(int pageNumber) {
        if (mChatAdapter.getDataSize() != mDataCount) {
            mChatAdapter.setHeaderVisibility(View.VISIBLE);
            int offset = pageNumber * PAGE_SIZE;
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


    /**
     * @return id of another user, if current chat is 1-1 conversation.
     */
    private String getUserId() {
        if (mIsGroupChat) {
            return null;
        }
        return String.valueOf(mConversationId);
    }

    /**
     * @return id of group conversation if current chat is group conversation.
     */
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
