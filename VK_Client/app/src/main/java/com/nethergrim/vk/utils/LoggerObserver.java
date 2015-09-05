package com.nethergrim.vk.utils;

import android.util.Log;

import com.nethergrim.vk.models.WebResponse;

import java.net.UnknownHostException;

import rx.Observer;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 05.09.15.
 */
public class LoggerObserver implements Observer<WebResponse> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            // ignore, just no internet connection
        } else {
            Log.e("WebError", e.toString() + " " + e.getMessage());
            // TODO: 05.09.15 add analytics handling here
        }
    }

    @Override
    public void onNext(WebResponse webResponse) {

    }
}
