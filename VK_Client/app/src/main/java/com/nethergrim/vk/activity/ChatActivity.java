package com.nethergrim.vk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.R;
import com.nethergrim.vk.fragment.ChatFragment;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.push.PushMessage;
import com.nethergrim.vk.utils.ConversationUtils;

import org.parceler.Parcels;

/**
 * Activity for screen with conversation. By default will open {@link ChatFragment} as a main
 * {@link
 * android.app.Fragment}
 *
 * @author Andrey Drobyazko (c2q9450@gmail.com)
 *         All rights reserved.
 */
public class ChatActivity extends AbstractActivity {

    private static final String EXTRA_PUSH_MESSAGE = "epm";


    public static Intent getIntentForReplyAction(Context context, PushMessage pushMessage) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_PUSH_MESSAGE, Parcels.wrap(pushMessage));
        return intent;
    }

    public static void start(Context context, Conversation conversation) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.EXTRA_CONVERSATION_ID, conversation.getId());
        intent.putExtra(Constants.EXTRA_GROUP_CONVERSATION,
                ConversationUtils.isConversationAGroupChat(conversation));
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = getIntent().getExtras();
        mWebIntentHandler.fetchStickers();
        if (extras != null) {
            if (extras.containsKey(Constants.EXTRA_CONVERSATION_ID)) {
                long mConversationId = extras.getLong(Constants.EXTRA_CONVERSATION_ID);
                boolean isGroupConversation = extras.getBoolean(Constants.EXTRA_GROUP_CONVERSATION);
                showFragment(ChatFragment.getInstance(mConversationId, isGroupConversation), false,
                        false, R.id.root);
            } else if (extras.containsKey(EXTRA_PUSH_MESSAGE)) {
                handleExtrasForReplyToPush(
                        Parcels.unwrap(extras.getParcelable(EXTRA_PUSH_MESSAGE)));
            }
        }

    }

    private void handleExtrasForReplyToPush(PushMessage pushMessage) {
        // TODO: 30.08.15 remove notification with this current message
        // notification id = message.getUid().hashCode()
        // TODO: 30.08.15 handle it
    }
}
