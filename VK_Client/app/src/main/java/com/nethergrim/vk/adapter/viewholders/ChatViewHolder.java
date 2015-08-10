package com.nethergrim.vk.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class ChatViewHolder extends RecyclerView.ViewHolder {


    TextView textBody;
    View bubbleView;
    TextView textDate;
    ImageView imageAvatar;

    public ChatViewHolder(View itemView) {
        super(itemView);
    }
}
