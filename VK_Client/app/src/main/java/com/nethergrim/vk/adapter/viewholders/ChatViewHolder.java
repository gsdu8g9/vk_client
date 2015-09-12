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
 * com.nethergrim.vk.adapter.ChatAdapter}
 *
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class ChatViewHolder extends RecyclerView.ViewHolder {


    @InjectView(R.id.textBody)
    public TextView textBody;
    @InjectView(R.id.avatar)
    public UserImageView imageAvatar;
    @InjectView(R.id.textDate)
    public TextView textDate;

    public ChatViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
