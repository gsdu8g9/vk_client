package com.nethergrim.vk.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nethergrim.vk.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andrej on 28.07.15.
 */
public class FriendsViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.image_avatar)
    public ImageView mImageView;
    @InjectView(R.id.textViewName)
    public TextView mTextViewName;

    public FriendsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
