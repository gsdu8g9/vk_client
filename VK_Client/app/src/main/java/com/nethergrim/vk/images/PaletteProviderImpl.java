package com.nethergrim.vk.images;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.models.UserPalette;
import com.nethergrim.vk.utils.UserPaletteUtils;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
@SuppressWarnings("unused")
public class PaletteProviderImpl implements PaletteProvider {

    public static final String TAG = PaletteProviderImpl.class.getSimpleName();
    private static final int MIN_DELAY_TO_UPDATE_PALETTE_MS = 1000 * 60 * 60 * 24 * 2; // 2 days

    @Inject
    ImageLoader mImageLoader;

    public PaletteProviderImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public void generateAndStorePaletteImmediately(@NonNull User user) {
        if (doesPaletteNeedToBeUpdated(user.getId())) {
            Bitmap bitmap = mImageLoader.getBitmap(user).toBlocking().first();
            if (bitmap == null) {
                Log.e(TAG, "generateAndStorePalette: bitmap is null");
                return;
            }
            Palette palette = Palette.from(bitmap).generate();
            final UserPalette userPalette = UserPaletteUtils.convert(palette, user.getId(), 0);
            Realm.getDefaultInstance().executeTransaction(realm -> realm.copyToRealmOrUpdate(userPalette));
        }
    }


    @Override
    public void generateAndStorePalette(@NonNull final List<User> userList) {
        Observable.from(userList)
                .subscribeOn(Schedulers.io())
                .doOnNext(this::generateAndStorePaletteImmediately)
                .subscribe();
    }

    @Override
    public int getPaletteColor(long userId) {
        UserPalette userPalette = getUserPalette(userId);
        int colorDefault = MyApplication.getInstance().getResources().getColor(R.color.primary_light);
        int color;
        if (userPalette == null) {
            Log.e(TAG, "getPaletteColor: no palette");
            return colorDefault;
        }
        color = userPalette.getVibrant();
        if (color == 0)
            color = userPalette.getVibrantDark();
        if (color == 0)
            color = userPalette.getVibrantLight();
        if (color == 0)
            color = userPalette.getMuted();
        if (color == 0)
            color = userPalette.getMutedDark();
        if (color == 0)
            color = userPalette.getMutedLight();
        if (color == 0)
            color = colorDefault;
        return color;
    }

    @Override
    public UserPalette getUserPalette(long userId) {
        return Realm.getDefaultInstance()
                .where(UserPalette.class)
                .equalTo("userId", userId)
                .findFirst();
    }

    private boolean isPaletteSavedForUser(long userId) {
        return getUserPalette(userId) != null;
    }


    private boolean doesPaletteNeedToBeUpdated(long userId) {
        UserPalette userPalette = getUserPalette(userId);
        return userPalette == null || (userPalette.getGeneratedAt() + MIN_DELAY_TO_UPDATE_PALETTE_MS
                < System.currentTimeMillis());
    }
}
