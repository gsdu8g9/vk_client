package com.nethergrim.vk.web;

import com.nethergrim.vk.models.WebError;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 04.09.15.
 */
public class VkApiError extends Error {

    private WebError mWebError;

    public VkApiError(WebError webError) {
        mWebError = webError;
    }

    public WebError getWebError() {
        return mWebError;
    }

    public void setWebError(WebError webError) {
        mWebError = webError;
    }
}
