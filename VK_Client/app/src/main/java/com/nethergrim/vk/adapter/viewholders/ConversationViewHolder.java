package com.nethergrim.vk.adapter.viewholders;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.UltimateAdapter;
import com.nethergrim.vk.views.imageViews.UserImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder} for {@link
 * com.nethergrim.vk.adapter.ConversationsAdapter}.
 *
 * @author andreydrobyazko on 4/6/15 (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class ConversationViewHolder extends UltimateAdapter.DataVH {

    @InjectView(R.id.image_avatar)
    public UserImageView imageAvatar;
    @InjectView(R.id.text_date)
    public TextView textDate;
    @InjectView(R.id.text_details)
    public TextView textDetails;
    @InjectView(R.id.text_name)
    public TextView textName;
    @InjectView(R.id.online_indicator)
    public View mOnlineIndicator;
    @InjectView(R.id.imageViewDetails)
    public SimpleDraweeView mImageViewDetails;

    public ConversationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
