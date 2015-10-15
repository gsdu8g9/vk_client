package com.nethergrim.vk.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nethergrim.vk.R;
import com.nethergrim.vk.models.StickerDbItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 15.10.15.
 */
@SuppressLint("ViewConstructor")
public class StickersLayoutView extends FrameLayout {

    @InjectView(R.id.backgroundDraweeView)
    SimpleDraweeView mBackgroundDraweeView;
    @InjectView(R.id.grid)
    GridView mGrid;

    private StickerDbItem mSticker;

    public StickersLayoutView(Context context, StickerDbItem stickerId) {
        super(context);
        this.mSticker = stickerId;
        if (!isInEditMode()) {
            init(context);
        }
    }


    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_stickers, this, true);
        ButterKnife.inject(this, v);
        mBackgroundDraweeView.setImageURI(Uri.parse(mSticker.getBackground()));
    }
}
