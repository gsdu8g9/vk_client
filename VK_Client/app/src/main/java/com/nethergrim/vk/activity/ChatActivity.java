package com.nethergrim.vk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.R;
import com.nethergrim.vk.fragment.ChatFragment;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.utils.ConversationUtils;

public class ChatActivity extends AbstractActivity {


    public static void start(Context context, Conversation conversation) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.EXTRA_CONVERSATION_ID, conversation.getId());
        intent.putExtra(Constants.EXTRA_GROUP_CONVERSATION,
                ConversationUtils.isConversationAGroupChat(conversation));
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(Constants.EXTRA_CONVERSATION_ID)) {
                long mConversationId = extras.getLong(Constants.EXTRA_CONVERSATION_ID);
                boolean isGroupConversation = extras.getBoolean(Constants.EXTRA_GROUP_CONVERSATION);
                showFragment(ChatFragment.getInstance(mConversationId, isGroupConversation), false,
                        false, R.id.root);
            }
        }

    }

}
