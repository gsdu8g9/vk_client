package com.nethergrim.vk.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nethergrim.vk.R;
import com.nethergrim.vk.models.RealmLong;
import com.nethergrim.vk.models.StickerDbItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class StickerAdapter extends ArrayAdapter<RealmLong> {

    private StickerDbItem mStickerDbItem;

    public StickerAdapter(Context context, StickerDbItem stickerDbItem) {
        super(context, 0, stickerDbItem.getStickerIds());
        this.mStickerDbItem = stickerDbItem;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        StickerViewHolder vh;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.vh_sticker, parent, false);
            vh = new StickerViewHolder(v);
            v.setTag(vh);
        } else {
            vh = (StickerViewHolder) v.getTag();
        }
        vh.mDraweeView.setImageURI(Uri.parse(mStickerDbItem.getUrls().get(position).getS()));
        return v;
    }

    public static class StickerViewHolder {

        @InjectView(R.id.drawee)
        SimpleDraweeView mDraweeView;

        public StickerViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}
