package com.nethergrim.vk.web;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public class ImageLoaderImpl implements ImageLoader {

    private Context context;

    public ImageLoaderImpl(Context context) {
        this.context = context;
    }

    @Override
    public void displayAvatar(String url, ImageView imageView) {
        Log.e("TAG", "displaying: " + url);
        Picasso.with(context).load(url).fit().centerCrop().into(imageView);
    }
}
