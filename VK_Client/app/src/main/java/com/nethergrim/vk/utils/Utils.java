package com.nethergrim.vk.utils;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.nethergrim.vk.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

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

    public static String generateAndroidId() {
        return Settings.Secure.getString(
                MyApplication.getInstance().getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static JSONObject convertBundleToJson(Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                // json.put(key, bundle.get(key)); see edit below
                json.put(key, JSONObject.wrap(bundle.get(key)));
            } catch (JSONException e) {
                //Handle exception here
            }
        }
        return json;
    }

}
