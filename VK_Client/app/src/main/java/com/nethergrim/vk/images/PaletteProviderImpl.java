package com.nethergrim.vk.images;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.models.UserPalette;
import com.nethergrim.vk.utils.UserPaletteUtils;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class PaletteProviderImpl implements PaletteProvider {

    public static final int MIN_DELAY_TO_UPDATE_PALETTE_MS = 1000 * 60 * 60 * 24 * 2; // 2 days

    @Inject
    ImageLoader mImageLoader;

    public PaletteProviderImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public void generateAndStorePalette(@NonNull User user) {
        if (doesPaletteNeedToBeUpdated(user.getId())) {
            Bitmap bitmap = mImageLoader.getBitmap(user);
            if (bitmap != null) {
                Palette palette = Palette.from(bitmap).generate();
                final UserPalette userPalette = UserPaletteUtils.convert(palette, user.getId(),
                        MyApplication.getInstance().getResources().getColor(
                                R.color.primary_light));
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(userPalette);
                    }
                });

            }
        }
    }

    @Override
    public void generateAndStorePalette(@NonNull final List<User> userList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                for (int i = 0, size = userList.size(); i < size; i++) {
                    generateAndStorePalette(userList.get(i));
                }
            }
        }).start();
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
