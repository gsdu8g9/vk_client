package com.nethergrim.vk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.ConversationUtils;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.web.DataManager;
import com.nethergrim.vk.web.WebRequestManager;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;

public class ChatActivity extends AbstractActivity {


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

    private long mConversationId;
    private Conversation mConversation;
    private boolean mIsGroupChat;

    private User mAnotherUser;

    public static void start(Context context, Conversation conversation) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.EXTRA_CONVERSATION_ID, conversation.getId());
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // TODO handle menu button tap
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(Constants.EXTRA_CONVERSATION_ID)) {
                mConversationId = extras.getLong(Constants.EXTRA_CONVERSATION_ID);
            }
        }
        MyApplication.getInstance().getMainComponent().inject(this);
        loadConversation();
        initToolbar();
        loadLastMessages();
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
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getToolbarTitle());
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
