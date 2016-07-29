package com.nethergrim.vk.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.json.JsonDeserializer;
import com.nethergrim.vk.models.push.PushObject;

import org.json.JSONObject;

import javax.inject.Inject;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 23.07.15.
 */
public class PushParserImpl implements PushParser {

    @Inject
    JsonDeserializer mJsonDeserializer;

    public PushParserImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public PushObject parsePushObject(@NonNull Bundle bundle) {
        PushObject.PushType pushType = getPushTypeForData(bundle);
        return parsePushObject(bundle, pushType);
    }

    @Override
    public PushObject parsePushObject(@NonNull Bundle bundle,
            @NonNull PushObject.PushType pushType) {
        switch (pushType) {
            case Message:
                JSONObject messageObject = Utils.convertBundleToJson(bundle);
                return mJsonDeserializer.getPushMessage(messageObject);
        }
        // return no object for not identified push notification type
        return null;
    }

    private PushObject.PushType getPushTypeForData(@NonNull Bundle data) {
        String collapseKey = data.getString("collapse_key");
        for (PushObject.PushType pushType : PushObject.PushType.values()) {
            if (pushType.getCollapseKey().equalsIgnoreCase(collapseKey)) {
                return pushType;
            }
        }
        return PushObject.PushType.Other; // not identified push type
    }
}
