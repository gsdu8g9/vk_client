package com.nethergrim.vk.web;

import com.vk.sdk.api.VKError;

/**
 * @author andreydrobyazko on 4/3/15.
 */
public interface WebCallback<T> {

    public void onResponseSucceed(T response);
    public void onResponseFailed(VKError e);
}
