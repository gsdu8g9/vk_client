package com.nethergrim.vk.adapter;

import android.text.format.DateUtils;
import android.util.LongSparseArray;
import android.view.View;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.viewholders.ChatViewHolder;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.Message;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserProvider;

import javax.inject.Inject;

import io.realm.RealmResults;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} that should be used to display list of
 * messages in {@link com.nethergrim.vk.fragment.ChatFragment}
 *
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class ChatAdapter extends SelectableUltimateAdapter
        implements UltimateAdapter.FooterInterface {

    public static final int TYPE_MY = 1;
    public static final int TYPE_NOT_MINE = 0;
    public static final long MAX_DELAY_TO_GROUP_MESSAGES_S = 90;
    @Inject
    UserProvider mUserProvider;
    @Inject
    Prefs mPrefs;
    private RealmResults<Message> mMessages;


    private LongSparseArray<User> mUsersMap; // key - Long, user id, value = user;

    private int mSelectedColor = -1;
    private int mUnreadColor;


    public ChatAdapter(RealmResults<Message> messages) {
        MyApplication.getInstance().getMainComponent().inject(this);
        this.mMessages = messages;
        setHasStableIds(true);
        mUsersMap = new LongSparseArray<>(20);
        mUnreadColor = MyApplication.getInstance().getResources().getColor(R.color.primary_light_opacity);
    }


    @Override
    public int getDataSize() {
        return mMessages.size();
    }

    @Override
    public int getDataViewResId(int viewType) {
        switch (viewType) {
            case TYPE_MY:
                return R.layout.vh_chat_right;
            case TYPE_NOT_MINE:
                return R.layout.vh_chat_left;
            default:
                return 0;
        }
    }

    @Override
    public long getDataId(int dataPosition) {
        if (dataPosition >= mMessages.size() || dataPosition < 0) {
            return -1;
        }
        return mMessages.get(dataPosition).getId();
    }

    @Override
    public int getDataViewType(int dataPosition) {
        if (dataPosition >= mMessages.size()) {
            return -5;
        }
        Message message = mMessages.get(dataPosition);
        return message.getOut() == 1 ? TYPE_MY : TYPE_NOT_MINE;
    }

    @Override
    public DataVH getDataViewHolder(View v, int dataViewType) {
        if (mSelectedColor == -1) {
            mSelectedColor = v.getContext().getResources().getColor(R.color.primary_light);
        }
        return new ChatViewHolder(v);
    }

    @Override
    public void bindDataVH(DataVH vh, int dataPosition) {
        ChatViewHolder chatViewHolder = (ChatViewHolder) vh;
        Message message = mMessages.get(dataPosition);
        User user = getUserById(message.getFrom_id());
        boolean displayAvatarAndSpace = shouldDisplaySpaceAndAvatar(dataPosition, message);
        boolean shouldDisplayDate = shouldDisplayDate(dataPosition, message);
        if (displayAvatarAndSpace) {
            chatViewHolder.imageAvatar.setVisibility(View.VISIBLE);
            chatViewHolder.spaceTop.setVisibility(View.VISIBLE);
            chatViewHolder.imageAvatar.display(user, true);
        } else {
            chatViewHolder.spaceTop.setVisibility(View.GONE);
            chatViewHolder.imageAvatar.setVisibility(View.INVISIBLE);
        }
        if (shouldDisplayDate) {
            chatViewHolder.textDate.setVisibility(View.VISIBLE);
            chatViewHolder.textDate.setText(
                    DateUtils.getRelativeTimeSpanString(message.getDate() * 1000));
        } else {
            chatViewHolder.textDate.setVisibility(View.INVISIBLE);
        }
        chatViewHolder.textBody.setText(message.getBody());
        if (isSelected(dataPosition)) {
            chatViewHolder.root.setBackgroundResource(R.color.green);
            chatViewHolder.textDate.setVisibility(View.GONE);
        } else {
            chatViewHolder.textDate.setVisibility(View.VISIBLE);
            chatViewHolder.root.setBackgroundColor(message.isRead_state() == 0 ? mUnreadColor : 0);
        }
    }

    @Override
    public FooterVH getFooterVH(View v) {
        return new MyFooterVH(v);
    }

    @Override
    public int getFooterViewResId() {
        return R.layout.spinner;
    }

    @Override
    public void bindFooterVH(FooterVH vh) {
        // nothing here, just empty spinner
    }

    private boolean shouldDisplaySpaceAndAvatar(int position, Message currentMessage) {
        // if view before this view is from same user, and was sent in one minute delay, return
        // false

        if (position == mMessages.size() - 1) {
            return true;
        }
        Message messageBeforeCurrent = mMessages.get(position + 1);
        if (currentMessage.getFrom_id() == messageBeforeCurrent.getFrom_id()) {
            long timeDelta = Math.abs(currentMessage.getDate() - messageBeforeCurrent.getDate());
            if (timeDelta < MAX_DELAY_TO_GROUP_MESSAGES_S) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldDisplayDate(int position, Message currentMessage) {
        if (position == mMessages.size() - 1) {
            return true;
        }
        if (position == 0) {
            return true;
        }
        Message messageAfterCurrent = mMessages.get(position - 1);
        long timeFutureDelta = Math.abs(currentMessage.getDate() - messageAfterCurrent.getDate());
        if (messageAfterCurrent.getFrom_id() == currentMessage.getFrom_id()
                && timeFutureDelta < MAX_DELAY_TO_GROUP_MESSAGES_S) {
            return false;
        }
        return true;
    }

    private User getUserById(long id) {
        User user = mUsersMap.get(id);
        if (user == null) {
            user = mUserProvider.getUser(id);
            mUsersMap.put(id, user);
        }
        return user;
    }

    public RealmResults<Message> getMessages() {
        return mMessages;
    }

    /**
     * @return the ID of last message, or -1 if no messages in adapter.
     */
    public long getLastMessageId() {
        if (mMessages == null || mMessages.isEmpty()) {
            return -1L;
        }
        Message lastMessage = mMessages.get(0);
        long id = lastMessage.getId();
        return id;
    }

    public static class MyFooterVH extends FooterVH {

        public MyFooterVH(View itemView) {
            super(itemView);
        }
    }
}
