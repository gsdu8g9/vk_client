package com.nethergrim.vk.adapter.viewholders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.UltimateAdapter;
import com.nethergrim.vk.views.imageViews.UserImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder} for {@link
 * com.nethergrim.vk.adapter.ChatAdapter}
 *
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class ChatViewHolder extends UltimateAdapter.DataVH {


    @InjectView(R.id.textBody)
    public TextView textBody;
    @InjectView(R.id.avatar)
    @Optional
    public UserImageView imageAvatar;
    @InjectView(R.id.textDate)
    public TextView textDate;
    @InjectView(R.id.top)
    public Space spaceTop;
    @InjectView(R.id.root)
    public RelativeLayout root;

    public ChatViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void hideAvatar() {
        if (imageAvatar != null) {
            imageAvatar.setVisibility(View.INVISIBLE);
        }
    }

    public void showAvatar() {
        if (imageAvatar != null) {
            imageAvatar.setVisibility(View.VISIBLE);
        }
    }
}
