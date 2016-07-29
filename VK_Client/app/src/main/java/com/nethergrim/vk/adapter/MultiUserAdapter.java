package com.nethergrim.vk.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.viewholders.MultiUserVH;
import com.nethergrim.vk.models.User;

import java.util.HashSet;
import java.util.Set;

import io.realm.RealmResults;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 05.09.15.
 */
public class MultiUserAdapter extends RecyclerView.Adapter<MultiUserVH>
        implements View.OnClickListener {

    private RealmResults<User> mData;
    private Set<Long> mSelectedItems = new HashSet<>();
    private int mSelectedColor = -1;

    public MultiUserAdapter(RealmResults<User> data) {
        this.mData = data;
    }

    @Override
    public MultiUserVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mSelectedColor == -1) {
            mSelectedColor = parent.getContext().getResources().getColor(R.color.primary_light);
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vh_multi_user, parent, false);
        return new MultiUserVH(v);
    }

    @Override
    public void onBindViewHolder(MultiUserVH holder, int position) {
        User user = mData.get(position);
        boolean selected = isSelected(user);
        holder.mUserImageView.display(user, true);
        holder.mTextName.setText(user.getFirstName() + " " + user.getLastName());
        holder.itemView.setBackgroundColor(selected ? mSelectedColor : Color.TRANSPARENT);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(user.getId());
    }

    @Override
    public long getItemId(int position) {
        if (mData == null || mData.size() <= position) {
            return 0l;
        }
        return mData.get(position).getId();
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        long id = (long) v.getTag();
        if (mSelectedItems.contains(id)) {
            // is selected now, deselect and update.
            mSelectedItems.remove(id);
        } else {
            // is not selected, select and update.
            mSelectedItems.add(id);
        }
        notifyDataSetChanged();
    }

    public Set<Long> getSelectedUserIds() {
        return mSelectedItems;
    }

    private boolean isSelected(User user) {
        return mSelectedItems.contains(user.getId());
    }
}
