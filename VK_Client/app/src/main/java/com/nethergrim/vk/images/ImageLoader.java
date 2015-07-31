package com.nethergrim.vk.images;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.widget.ImageView;

import com.nethergrim.vk.models.User;
import com.squareup.picasso.Target;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public interface ImageLoader {

    void displayUserAvatar(@NonNull User user, @NonNull ImageView imageView);

    void displayImage(@NonNull String url, @NonNull ImageView imageView);

    void getUserAvatar(@NonNull User user, @NonNull Target target);

    void cacheImage(@NonNull String url);

    void cacheUserAvatars(@NonNull User user);

    @WorkerThread
    Bitmap getBitmap(@NonNull String url);

    @WorkerThread
    Bitmap getBitmap(@NonNull User user);

}
