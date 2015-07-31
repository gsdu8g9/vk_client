package com.nethergrim.vk.utils;

import android.support.v7.graphics.Palette;

import com.nethergrim.vk.models.UserPalette;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class UserPaletteUtils {

    public static UserPalette convert(Palette palette, long userId, int defaultColor) {
        UserPalette userPalette = new UserPalette();
        userPalette.setMuted(palette.getMutedColor(defaultColor));
        userPalette.setMutedDark(palette.getDarkMutedColor(defaultColor));
        userPalette.setMutedLight(palette.getLightMutedColor(defaultColor));
        userPalette.setUserId(userId);
        userPalette.setVibrant(palette.getVibrantColor(defaultColor));
        userPalette.setVibrantLight(
                palette.getLightVibrantColor(defaultColor));
        userPalette.setVibrantDark(
                palette.getDarkVibrantColor(defaultColor));
        userPalette.setGeneratedAt(System.currentTimeMillis());
        return userPalette;
    }

}
