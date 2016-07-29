package com.nethergrim.vk.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nethergrim.vk.R;
import com.nethergrim.vk.views.imageViews.UserImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 05.09.15.
 */
public class MultiUserVH extends RecyclerView.ViewHolder {

    @InjectView(R.id.avatar)
    public UserImageView mUserImageView;

    @InjectView(R.id.text_name)
    public TextView mTextName;

    public MultiUserVH(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
