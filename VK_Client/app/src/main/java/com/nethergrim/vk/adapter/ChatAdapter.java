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

    private static final int TYPE_MY = R.layout.vh_chat_right;
    private static final int TYPE_NOT_MINE = R.layout.vh_chat_left;
    private static final long MAX_DELAY_TO_GROUP_MESSAGES_S = 60 * 10;
    @Inject
    UserProvider mUserProvider;
    @Inject
    Prefs mPrefs;
    private RealmResults<Message> mMessages;


    private LongSparseArray<User> mUsersMap; // key - Long, user id, value = user;

    private int mSelectedColor = -1;
    private int mUnreadColor;


    @SuppressWarnings("deprecation")
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
        return viewType;
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
            return TYPE_MY;
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
    public void bindDataVH(DataVH viewHolder, int dataPosition) {
        ChatViewHolder vh = (ChatViewHolder) viewHolder;
        Message message = mMessages.get(dataPosition);
        User user = getUserById(message.getFrom_id());
        vh.textBody.setText(message.getBody());
        boolean displayAvatarAndSpace = shouldDisplaySpaceAndAvatar(dataPosition, message);

        if (displayAvatarAndSpace) {
            vh.showAvatar();
            vh.spaceTop.setVisibility(View.VISIBLE);
            if (vh.imageAvatar != null) {
                vh.imageAvatar.display(user, true);
            }
        } else {
            vh.hideAvatar();
            vh.spaceTop.setVisibility(View.GONE);
        }

        if (isSelected(dataPosition)) {
            vh.root.setBackgroundResource(R.color.green);
            vh.textDate.setVisibility(View.GONE);
        } else {
            vh.textDate.setVisibility(View.VISIBLE);
            if (!message.isMine()) {
                vh.root.setBackgroundColor(message.isRead_state() == 0 ? mUnreadColor : 0);
            }

            if (message.isMine()) {
                if (message.isPending()) {
                    // pending
                    vh.textDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_present_to_all_black_16px, 0);
                } else if (message.isRead_state() == 1) {
                    // read
                    vh.textDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_all_black_24px, 0);
                } else {
                    // sent
                    vh.textDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24px, 0);
                }
            }
        }


        vh.textDate.setVisibility(View.VISIBLE);
        vh.textDate.setText(DateUtils.getRelativeTimeSpanString(message.getDate() * 1000));
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
        long timeFutureDelta = currentMessage.getDate() - messageAfterCurrent.getDate();
        return !(messageAfterCurrent.getFrom_id() == currentMessage.getFrom_id()
                && timeFutureDelta < MAX_DELAY_TO_GROUP_MESSAGES_S);
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
        for (int i = 0, mMessagesSize = mMessages.size(); i < mMessagesSize; i++) {
            Message mMessage = mMessages.get(i);
            if (!mMessage.isPending()) {
                return mMessage.getId();
            }
        }
        return -1L;
    }

    private static class MyFooterVH extends FooterVH {

        MyFooterVH(View itemView) {
            super(itemView);
        }
    }
}
