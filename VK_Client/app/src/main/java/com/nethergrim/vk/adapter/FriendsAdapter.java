package com.nethergrim.vk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.viewholders.FriendsViewHolder;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.event.UsersUpdatedEvent;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.web.images.ImageLoader;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * @author andrej on 28.07.15.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder>
        implements RealmChangeListener {

    @Inject
    Bus mBus;

    @Inject
    Prefs mPrefs;
    @Inject
    ImageLoader mImageLoader;

    private RealmResults<User> mData;

    public FriendsAdapter() {
        MyApplication.getInstance().getMainComponent().inject(this);
        mBus.register(this);
        Realm realm = Realm.getDefaultInstance();
        mData = realm.where(User.class)
                .notEqualTo("id", mPrefs.getCurrentUserId())
                .equalTo("friend_status", 3)
                .or()
                .equalTo("is_friend", 1)
                .findAllSorted("firstName", true);
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
        Context ctx = holder.itemView.getContext();
        User user = mData.get(position);
        String avatarUrl = user.getPhoto_200();
        if (TextUtils.isEmpty(avatarUrl)) {
            avatarUrl = user.getPhoto_200_orig();
        }
        mImageLoader.displayImage(avatarUrl, holder.mImageView);
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
}
