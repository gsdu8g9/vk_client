package com.nethergrim.vk.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.viewholders.FriendsViewHolder;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.event.UsersUpdatedEvent;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} that should be used to display list of
 * friends at {@link com.nethergrim.vk.fragment.FriendsFragment}
 *
 * @author Andrey Drobyazko (c2q9450@gmail.com)
 *         All rights reserved.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder>
        implements RealmChangeListener, View.OnClickListener {

    @Inject
    Bus mBus;

    @Inject
    Prefs mPrefs;
    @Inject
    ImageLoader mImageLoader;

    private RealmResults<User> mData;
    private OnFriendClickedCallback mOnFriendClickedCallback;

    /**
     * Callback interface, to be notified when user was selected from {@link FriendsAdapter}
     */
    public interface OnFriendClickedCallback {

        /**
         * @param v View that was clicked by user.
         * @param user User that was selected by click.
         */
        void onFriendClicked(View v, User user);
    }

    public FriendsAdapter(@Nullable OnFriendClickedCallback callback) {
        this.mOnFriendClickedCallback = callback;
        MyApplication.getInstance().getMainComponent().inject(this);
        mBus.register(this);
        Realm realm = Realm.getDefaultInstance();
        mData = realm.where(User.class)
                .notEqualTo("id", mPrefs.getCurrentUserId())
                .equalTo("friend_status", 3)
                .findAllSorted("friendRating", true);
        notifyDataSetChanged();
        realm.addChangeListener(this);
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vh_friend_grid, parent, false);
        return new FriendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        User user = mData.get(position);
        String avatarUrl = UserUtils.getStablePhotoUrl(user);
        mImageLoader.displayImage(avatarUrl, holder.mImageView);
        holder.mTextViewName.setText(user.getFirstName() + "\n " + user.getLastName());
        holder.itemView.setTag(Integer.valueOf(position));
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }

    @Subscribe
    public void onDataChanged(UsersUpdatedEvent e) {
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        if (position != null && mOnFriendClickedCallback != null && mData.size() > position) {
            mOnFriendClickedCallback.onFriendClicked(v, mData.get(position));
        }
    }
}
