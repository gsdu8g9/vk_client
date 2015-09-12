package com.nethergrim.vk.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    public static final int TYPE_MY = 1;
    public static final int TYPE_NOT_MINE = 0;
    @Inject
    UserProvider mUserProvider;
    @Inject
    Prefs mPrefs;
    private long mMyId;
    private RealmResults<Message> mMessages;

    // key - Long, user id, value = user;
    private Map<Long, User> mUsersMap;


    public ChatAdapter(long conversationId, boolean isAGroupChat) {
        MyApplication.getInstance().getMainComponent().inject(this);
        Realm realm = Realm.getDefaultInstance();
        setHasStableIds(true);
        mUsersMap = new HashMap<>();
        mMyId = mPrefs.getCurrentUserId();
        if (isAGroupChat) {
            mMessages = realm.where(Message.class)
                    .equalTo("chat_id", conversationId)
                    .findAllSorted("date");
        } else {
            mMessages = realm.where(Message.class)
                    .equalTo("user_id", conversationId)
                    .findAllSorted("date");
        }
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_MY) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vh_chat_right, parent, false);
        } else if (viewType == TYPE_NOT_MINE) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vh_chat_left, parent, false);
        } else {
            v = null;
        }
        return new ChatViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Message message = mMessages.get(position);
        int type = getItemViewType(position);
        User user = getUserById(message.getFrom_id());

        holder.textBody.setText(message.getBody());
        holder.imageAvatar.display(user, true);
        holder.textDate.setText(DateUtils.getRelativeTimeSpanString(message.getDate() * 1000));

    }

    @Override
    public int getItemViewType(int position) {
        if (position >= mMessages.size()) {
            return -1;
        }
        Message message = mMessages.get(position);
        return message.getOut() == 1 ? TYPE_MY : TYPE_NOT_MINE;
    }

    @Override
    public long getItemId(int position) {
        if (position >= mMessages.size()) {
            return -1;
        }
        return mMessages.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    private User getUserById(long id) {
        User user = mUsersMap.get(id);
        if (user == null) {
            user = mUserProvider.getUser(id);
            mUsersMap.put(id, user);
        }
        return user;
    }
}
