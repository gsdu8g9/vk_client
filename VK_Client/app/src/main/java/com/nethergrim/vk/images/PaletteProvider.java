package com.nethergrim.vk.images;

import android.support.annotation.NonNull;

import com.nethergrim.vk.models.User;
import com.nethergrim.vk.models.UserPalette;

import java.util.List;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public interface PaletteProvider {

    void generateAndStorePalette(@NonNull User user);

    void generateAndStorePalette(@NonNull List<User> userList);

    UserPalette getUserPalette(long userId);

}
