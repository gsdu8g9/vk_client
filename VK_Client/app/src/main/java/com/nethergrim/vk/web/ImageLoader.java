package com.nethergrim.vk.web;

import android.widget.ImageView;

import com.nethergrim.vk.models.User;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public interface ImageLoader {

    @Deprecated
    void displayAvatar(String url, ImageView imageView);

    void diaplyAvatar(User user, ImageView imageView);
}
