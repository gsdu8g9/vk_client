package com.nethergrim.vk.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.StickerAdapter;
import com.nethergrim.vk.models.StickerDbItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import github.ankushsachdeva.emojicon.EmojiconGridView;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 15.10.15.
 */
@SuppressLint("ViewConstructor")
public class StickersLayoutView extends FrameLayout implements AdapterView.OnItemClickListener {

    @InjectView(R.id.backgroundDraweeView)
    SimpleDraweeView mBackgroundDraweeView;
    @InjectView(R.id.grid)
    GridView mGrid;

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
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_stickers, this, true);
        ButterKnife.inject(this, v);
        mBackgroundDraweeView.setImageURI(Uri.parse(mSticker.getBackground()));
        StickerAdapter stickerAdapter = new StickerAdapter(context, mSticker);
        mGrid.setNumColumns(GridView.AUTO_FIT);
        int spacing = context.getResources().getDimensionPixelSize(R.dimen.chat_sticker_spacing);
        mGrid.setVerticalSpacing(spacing);
        mGrid.setHorizontalSpacing(spacing);
        mGrid.setAdapter(stickerAdapter);
        mGrid.setOnItemClickListener(this);
    }
}
