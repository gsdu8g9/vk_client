package com.nethergrim.vk.images;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.nethergrim.vk.models.User;

import rx.Observable;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public interface ImageLoader {
    
    @NonNull
    Observable<Bitmap> getUserAvatar(@NonNull User user);

    @NonNull
    Observable<Bitmap> getBitmap(@NonNull String url);

    @NonNull
    Observable<Bitmap> getBitmap(@NonNull User user);


    void preCache(@NonNull String url);

    @Nullable
    @WorkerThread
    Bitmap getBitmapImmediately(@NonNull String url);

}
