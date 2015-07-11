package com.nethergrim.vk.web.images;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.nethergrim.vk.models.User;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public interface ImageLoader {

    void displayUserAvatar(@NonNull User user,@NonNull ImageView imageView);
}
