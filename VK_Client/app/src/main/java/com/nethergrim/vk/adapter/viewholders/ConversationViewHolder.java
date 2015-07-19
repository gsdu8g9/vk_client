package com.nethergrim.vk.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nethergrim.vk.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andreydrobyazko on 4/6/15.
 */
public class ConversationViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.image_avatar)
    public ImageView imageAvatar;
    @InjectView(R.id.text_date)
    public TextView textDate;
    @InjectView(R.id.text_details)
    public TextView textDetails;
    @InjectView(R.id.text_name)
    public TextView textName;
    @InjectView(R.id.online_indicator)
    public View mOnlineIndicator;

    public ConversationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
