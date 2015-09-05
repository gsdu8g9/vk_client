package com.nethergrim.vk.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.viewholders.ConversationViewHolder;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.Message;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.ConversationUtils;
import com.nethergrim.vk.utils.MessageUtils;
import com.nethergrim.vk.utils.UserProvider;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} that should be used to display list of
 * conversations in {@link com.nethergrim.vk.fragment.MessagesFragment}
 *
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class ConversationsAdapter extends RecyclerView.Adapter<ConversationViewHolder>
        implements RealmChangeListener, View.OnClickListener {

    @Inject
    UserProvider mUserProvider;
    @Inject
    Prefs mPrefs;
    @Inject
    Realm mRealm;

    private RealmResults<Conversation> mData;
    private OnConversationClickListener mCallback;

    private int textColorPrimary;
    private int textColorSecondary;

    public interface OnConversationClickListener {

        void onConversationClicked(Conversation conversation, View v);
    }

    public ConversationsAdapter(OnConversationClickListener callback) {
        this.mCallback = callback;
        MyApplication.getInstance().getMainComponent().inject(this);
        mRealm.setAutoRefresh(true);
        this.mData = mRealm.where(Conversation.class)
                .findAllSorted("date", false);
        setHasStableIds(true);
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (textColorPrimary == 0) {
            textColorPrimary = viewGroup.getResources().getColor(R.color.primary_text);
            textColorSecondary = viewGroup.getResources().getColor(R.color.secondary_text);
        }
        return new ConversationViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.vh_conversation, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder conversationViewHolder, int i) {
        Conversation conversation = mData.get(i);
        Message message = conversation.getMessage();
        String details = "";
        User user;
        Context ctx = conversationViewHolder.itemView.getContext();

        if (MessageUtils.isMessageWithPhoto(message)) {
            details = "[ " + ctx.getString(R.string.photo) + " ] " + details;
        }
        if (MessageUtils.isMessageWithAudio(message)) {
            details = "[ " + ctx.getString(R.string.audio) + " ] " + details;
        }
        if (MessageUtils.isMessageWithWall(message)) {
            details = "[ " + ctx.getString(R.string.wall_entry) + " ] " + details;
        }
        if (MessageUtils.isMessageWithReply(message)) {
            details = "[ " + ctx.getString(R.string.reply) + " ] " + details;
        }
        if (MessageUtils.isMessageWithDoc(message)) {
            details = "[ " + ctx.getString(R.string.document) + " ] " + details;
        }
        if (MessageUtils.isMessageWithVideo(message)) {
            details = "[ " + ctx.getString(R.string.video) + " ] " + details;
        }

        if (ConversationUtils.isConversationAGroupChat(conversation)) {

//            group chat
            conversationViewHolder.imageAvatar.displayGroupChat();
            conversationViewHolder.textName.setText(message.getTitle());

            if (ConversationUtils.isMessageFromMe(message)) {
                details = conversationViewHolder.itemView.getResources()
                        .getString(R.string.me_) + " " + details
                        + " " + message.getBody();
            } else {
                user = mUserProvider.getUser(message.getUser_id());
                if (user != null) {
                    details = user.getFirstName() + ": " + details + message.getBody();
                }

            }

            conversationViewHolder.mOnlineIndicator.setVisibility(View.GONE);
        } else {
//              regular chat
            user = mUserProvider.getUser(conversation.getId());

            details = details + message.getBody();
            if (ConversationUtils.isMessageFromMe(message)) {
                details = conversationViewHolder.itemView.getResources().getString(R.string.me_)
                        + " " + details;
            }

            if (user != null) {
                conversationViewHolder.mOnlineIndicator.setVisibility(
                        user.getOnline() == 1 ? View.VISIBLE : View.GONE);
                conversationViewHolder.imageAvatar.display(user, true);
                conversationViewHolder.textName.setText(
                        user.getFirstName() + " " + user.getLastName());
            }
        }
        if (MessageUtils.isMessageWithSticker(message)) {
            details = details + "[ " + ctx.getString(R.string.sticker) + " ]";
            conversationViewHolder.mImageViewDetails.setVisibility(View.VISIBLE);
            String url = MessageUtils.getStickerFromMessage(message).getPhoto256();
            conversationViewHolder.mImageViewDetails.setImageURI(Uri.parse(url));
        } else {
            conversationViewHolder.mImageViewDetails.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(details)) {
            conversationViewHolder.textDetails.setText(details);
            conversationViewHolder.textDetails.setVisibility(View.VISIBLE);
        } else {
            conversationViewHolder.textDetails.setVisibility(View.GONE);
        }

        conversationViewHolder.textDate.setText(
                DateUtils.getRelativeTimeSpanString(message.getDate() * 1000,
                        System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_ALL));
        if (ConversationUtils.isConversationUnread(conversation)) {
            conversationViewHolder.textName.setTextColor(textColorPrimary);
            conversationViewHolder.textDetails.setTextColor(textColorPrimary);
        } else {
            conversationViewHolder.textName.setTextColor(textColorSecondary);
            conversationViewHolder.textDetails.setTextColor(textColorSecondary);
        }
        conversationViewHolder.itemView.setOnClickListener(this);
        conversationViewHolder.itemView.setTag(i);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        Conversation conversation = mData.get(position);
        if (mCallback != null) {
            mCallback.onConversationClicked(conversation, v);
        }
    }
}
