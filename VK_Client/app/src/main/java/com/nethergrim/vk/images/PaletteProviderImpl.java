package com.nethergrim.vk.images;

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
import rx.schedulers.Schedulers;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class PaletteProviderImpl implements PaletteProvider {

    public static final String TAG = PaletteProviderImpl.class.getSimpleName();
    public static final int MIN_DELAY_TO_UPDATE_PALETTE_MS = 1000 * 60 * 60 * 24 * 2; // 2 days

    @Inject
    ImageLoader mImageLoader;

    public PaletteProviderImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public void generateAndStorePalette(@NonNull User user) {
        if (doesPaletteNeedToBeUpdated(user.getId())) {
            mImageLoader
                    .getBitmap(user)
                    .doOnNext(bitmap -> {
                        Palette palette = Palette.from(bitmap).generate();
                        final UserPalette userPalette = UserPaletteUtils.convert(palette,
                                user.getId(),
                                MyApplication.getInstance().getResources().getColor(
                                        R.color.primary_light));
                        Realm.getDefaultInstance().executeTransaction(
                                realm -> realm.copyToRealmOrUpdate(userPalette));
                    })
                    .doOnError(throwable -> Log.e(TAG, throwable.toString()))
                    .observeOn(Schedulers.computation());

        }
    }


    @Override
    public void generateAndStorePalette(@NonNull final List<User> userList) {
        new Thread(() -> {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            for (int i = 0, size = userList.size(); i < size; i++) {
                generateAndStorePalette(userList.get(i));
            }
        }).start();
    }

    @Override
    public int getPaletteColor(long userId) {
        UserPalette userPalette = getUserPalette(userId);
        int color;
        if (userPalette != null && userPalette.getVibrant() != 0) {
            color = userPalette.getVibrant();
        } else if (userPalette != null && userPalette.getMuted() != 0) {
            color = userPalette.getMuted();
        } else {
            color = MyApplication.getInstance().getResources().getColor(R.color.primary_light);
        }
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
