package com.nethergrim.vk.utils;

import android.os.Bundle;

import com.nethergrim.vk.models.push.PushObject;

/**
 * @author andrej on 23.07.15.
 */
public interface PushParser {

    PushObject parsePushObject(Bundle bundle);
}
