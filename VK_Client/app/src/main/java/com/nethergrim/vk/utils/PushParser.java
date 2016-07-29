package com.nethergrim.vk.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.nethergrim.vk.models.push.PushObject;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 23.07.15.
 */
public interface PushParser {

    PushObject parsePushObject(@NonNull Bundle bundle);

    PushObject parsePushObject(@NonNull Bundle bundle, @NonNull PushObject.PushType pushType);
}
