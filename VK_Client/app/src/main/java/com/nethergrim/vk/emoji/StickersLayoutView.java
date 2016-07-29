package com.nethergrim.vk.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.StickerAdapter;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.models.StickerDbItem;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import github.ankushsachdeva.emojicon.EmojiconGridView;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 15.10.15.
 */
@SuppressWarnings("WeakerAccess")
@SuppressLint("ViewConstructor")
public class StickersLayoutView extends FrameLayout implements AdapterView.OnItemClickListener {

    @InjectView(R.id.backgroundDraweeView)
    ImageView mBackgroundDraweeView;
    @InjectView(R.id.grid)
    GridView mGrid;

    @Inject
    ImageLoader imageLoader;

    private StickerDbItem mSticker;
    private EmojiconGridView.OnEmojiconClickedListener mCallback;

    public StickersLayoutView(Context context,
                              StickerDbItem stickerId,
                              EmojiconGridView.OnEmojiconClickedListener callback) {
        super(context);
        this.mCallback = callback;
        this.mSticker = stickerId;
        if (!isInEditMode()) {
            init(context);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCallback != null) {
            mCallback.onStickerClicked(mSticker.getStickerIds().get(position).getData());
        }
    }


    private void init(Context context) {
        MyApplication.getInstance().getMainComponent().inject(this);
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_stickers, this, true);
        ButterKnife.inject(this, v);
        imageLoader.loadImage(mSticker.getBackground(), mBackgroundDraweeView);
        StickerAdapter stickerAdapter = new StickerAdapter(context, mSticker);
        mGrid.setNumColumns(GridView.AUTO_FIT);
        int spacing = context.getResources().getDimensionPixelSize(R.dimen.chat_sticker_spacing);
        mGrid.setVerticalSpacing(spacing);
        mGrid.setHorizontalSpacing(spacing);
        mGrid.setAdapter(stickerAdapter);
        mGrid.setOnItemClickListener(this);
    }
}
