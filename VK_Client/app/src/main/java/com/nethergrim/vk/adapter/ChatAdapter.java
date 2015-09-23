package com.nethergrim.vk.adapter;

import android.text.format.DateUtils;
import android.view.View;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.viewholders.ChatViewHolder;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.Message;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserProvider;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} that should be used to display list of
 * messages in {@link com.nethergrim.vk.fragment.ChatFragment}
 *
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class ChatAdapter extends UltimateAdapter implements UltimateAdapter.HeaderInterface {

    public static final int TYPE_MY = 1;
    public static final int TYPE_NOT_MINE = 0;
    public static final long MAX_DELAY_TO_GROUP_MESSAGES_S = 90;
    @Inject
    UserProvider mUserProvider;
    @Inject
    Prefs mPrefs;
    private RealmResults<Message> mMessages;
    // key - Long, user id, value = user;
    private Map<Long, User> mUsersMap;


    public ChatAdapter(long conversationId, boolean isAGroupChat) {
        MyApplication.getInstance().getMainComponent().inject(this);
        Realm realm = Realm.getDefaultInstance();
        setHasStableIds(true);
        mUsersMap = new HashMap<>();
        if (isAGroupChat) {
            mMessages = realm.where(Message.class)
                    .equalTo("chat_id", conversationId)
                    .findAllSorted("date", false);
        } else {
            mMessages = realm.where(Message.class)
                    .equalTo("user_id", conversationId)
                    .findAllSorted("date", false);
        }
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
        if (dataPosition >= mMessages.size()) {
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

    }

    @Override
    public HeaderVH getHeaderVH(View v) {
        return new MyHeaderVH(v);
    }

    @Override
    public int getHeaderViewResId() {
        return R.layout.spinner;
    }

    @Override
    public void bindHeaderVH(HeaderVH vh) {
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

    public static class MyHeaderVH extends HeaderVH {

        public MyHeaderVH(View itemView) {
            super(itemView);
        }
    }
}
