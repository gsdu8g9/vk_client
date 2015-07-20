package com.nethergrim.vk.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.viewholders.ConversationViewHolder;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.ConversationUtils;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.web.images.ImageLoader;

import javax.inject.Inject;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * @author andreydrobyazko on 4/6/15.
 */
public class ConversationsAdapter extends RecyclerView.Adapter<ConversationViewHolder>
        implements RealmChangeListener {

    @Inject
    ImageLoader il;

    @Inject
    UserProvider mUP;

    @Inject
    Prefs mPrefs;

    private int mUnreadColor;

    private RealmResults<Conversation> data;

    public ConversationsAdapter(RealmResults<Conversation> data) {
        this.data = data;
        setHasStableIds(true);
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (mUnreadColor == 0) {
            mUnreadColor = viewGroup.getResources().getColor(R.color.conversation_row_unread);
        }
        return new ConversationViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.vh_conversation, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder conversationViewHolder, int i) {
        Conversation conversation = data.get(i);

        String details;
        User user;
        if (ConversationUtils.isConversationAGroupChat(conversation)) {
            conversationViewHolder.imageAvatar.setImageResource(
                    R.drawable.ic_social_people_outline);
            conversationViewHolder.textName.setText(conversation.getMessage().getTitle());

            user = mUP.getUser(conversation.getMessage().getFrom_id());

            if (user != null) {
                if (ConversationUtils.isMessageFromMe(conversation.getMessage())) {
                    details = conversationViewHolder.itemView.getResources().getString(R.string.me_)
                            + " " + conversation.getMessage().getBody();
                } else {
                    details = user.getFirstName() + ": " + conversation.getMessage().getBody();
                }

            } else {
                details = conversation.getMessage().getBody();
                Log.e("TAG",
                        "user with id: " + conversation.getMessage().getFrom_id() + " is null");
            }

            conversationViewHolder.mOnlineIndicator.setVisibility(View.GONE);
        } else {

            user = mUP.getUser(conversation.getId());

            details = conversation.getMessage().getBody();
            if (ConversationUtils.isMessageFromMe(conversation.getMessage())) {
                details = conversationViewHolder.itemView.getResources().getString(R.string.me_)
                        + " " + details;
            }

            if (user != null) {
                conversationViewHolder.mOnlineIndicator.setVisibility(
                        user.getOnline() == 1 ? View.VISIBLE : View.GONE);
                il.displayUserAvatar(user, conversationViewHolder.imageAvatar);
                conversationViewHolder.textName.setText(
                        user.getFirstName() + " " + user.getLastName());
            }
        }

        conversationViewHolder.textDetails.setText(details);
        conversationViewHolder.textDate.setText(
                DateUtils.getRelativeTimeSpanString(conversation.getMessage().getDate() * 1000,
                        System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_ALL));
        if (ConversationUtils.isConversationUnread(conversation)) {
            conversationViewHolder.itemView.setBackgroundColor(mUnreadColor);
        } else {
            conversationViewHolder.itemView.setBackgroundResource(0);
        }

    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }
}
