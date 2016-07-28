package com.nethergrim.vk.callbacks;

import com.vk.sdk.api.VKError;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 4/3/15.
 */
public interface WebCallback<T> {

    void onResponseSucceed(T response);

    void onResponseFailed(VKError e);
}
