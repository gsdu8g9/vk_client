package com.nethergrim.vk.callbacks;

import com.vk.sdk.api.VKError;

/**
 * @author andreydrobyazko on 4/3/15.
 */
public interface WebCallback<T> {

    void onResponseSucceed(T response);

    void onResponseFailed(VKError e);
}
