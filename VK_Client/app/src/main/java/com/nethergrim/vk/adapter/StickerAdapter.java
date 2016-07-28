package com.nethergrim.vk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.models.RealmLong;
import com.nethergrim.vk.models.StickerDbItem;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public class StickerAdapter extends ArrayAdapter<RealmLong> {

    @Inject
    ImageLoader imageLoader;

    private StickerDbItem mStickerDbItem;

    public StickerAdapter(Context context, StickerDbItem stickerDbItem) {
        super(context, 0, stickerDbItem.getStickerIds());
        this.mStickerDbItem = stickerDbItem;
        MyApplication.getInstance().getMainComponent().inject(this);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        StickerViewHolder vh;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.vh_sticker, parent, false);
            vh = new StickerViewHolder(v);
            v.setTag(vh);
        } else {
            vh = (StickerViewHolder) v.getTag();
        }
        imageLoader.loadImage(mStickerDbItem.getUrls().get(position).getS(), vh.imageView);

        if (getCount() > position + 4) {
            imageLoader.preCache(mStickerDbItem.getUrls().get(position + 3).getS());
        }
        return v;
    }

    static class StickerViewHolder {

        @InjectView(R.id.image)
        ImageView imageView;


        StickerViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}
