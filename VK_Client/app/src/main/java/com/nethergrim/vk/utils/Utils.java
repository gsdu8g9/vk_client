package com.nethergrim.vk.utils;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.nethergrim.vk.MyApplication;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class Utils {

    public static Drawable tintIcon(@DrawableRes int iconResId, @ColorRes int color) {
        color = MyApplication.getInstance().getResources().getColor(color);
        Drawable drawable = MyApplication.getInstance().getResources().getDrawable(iconResId);

        int red = (color & 0xFF0000) / 0xFFFF;
        int green = (color & 0xFF00) / 0xFF;
        int blue = color & 0xFF;

        float[] matrix = {0, 0, 0, 0, red
                , 0, 0, 0, 0, green
                , 0, 0, 0, 0, blue
                , 0, 0, 0, 1, 0};

        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(colorFilter);

        return drawable;
    }

}
