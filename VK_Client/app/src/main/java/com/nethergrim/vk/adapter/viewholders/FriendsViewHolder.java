package com.nethergrim.vk.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nethergrim.vk.R;
import com.nethergrim.vk.views.imageViews.UserImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder} for {@link
 * com.nethergrim.vk.adapter.FriendsAdapter}
 *
 * @author Andrey Drobyazko (c2q9450@gmail.com)
 *         All rights reserved.
 */
public class FriendsViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.image_avatar)
    public UserImageView mImageView;
    @InjectView(R.id.textViewName)
    public TextView mTextViewName;

    public FriendsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
