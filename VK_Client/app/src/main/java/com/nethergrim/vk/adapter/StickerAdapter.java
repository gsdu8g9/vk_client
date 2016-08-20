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
import com.nethergrim.vk.models.StickerLocal;
import com.nethergrim.vk.models.StickersCollectionLocal;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public class StickerAdapter extends ArrayAdapter<StickerLocal> {

    @Inject
    ImageLoader imageLoader;

    public StickerAdapter(Context context, StickersCollectionLocal stickerDbItem) {
        super(context, 0, stickerDbItem.getStickersList());
        MyApplication.getInstance().getMainComponent().inject(this);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        StickerViewHolder vh;
        StickerLocal stickerLocal = getItem(position);
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.vh_sticker, parent, false);
            vh = new StickerViewHolder(v);
            v.setTag(vh);
        } else {
            vh = (StickerViewHolder) v.getTag();
        }
        imageLoader.loadImage(stickerLocal.getUrl(), vh.imageView);

        if (getCount() > position + 4) {
            imageLoader.preCache(getItem(position + 2).getUrl());
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
