package com.nethergrim.vk.images;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nethergrim.vk.models.User;

import rx.Observable;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public interface ImageLoader {


    Observable<Bitmap> getUserAvatar(@NonNull User user);

    Observable<Bitmap> getBitmap(@NonNull String url);

    Observable<Bitmap> getBitmap(@NonNull User user);

    void cacheToMemory(String url);

    @Nullable
    Bitmap getBitmapSync(String url);

}
