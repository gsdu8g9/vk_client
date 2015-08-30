package com.nethergrim.vk.images;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.nethergrim.vk.models.User;

import rx.Observable;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public interface ImageLoader {

    void displayUserAvatar(@NonNull User user, @NonNull ImageView imageView);

    void displayImage(@NonNull String url, @NonNull ImageView imageView);

    Observable<Bitmap> getUserAvatar(@NonNull User user);

    void cacheImage(@NonNull String url);

    void cacheUserAvatars(@NonNull User user);

    Observable<Bitmap> getBitmap(@NonNull String url);

    Observable<Bitmap> getBitmap(@NonNull User user);

}
