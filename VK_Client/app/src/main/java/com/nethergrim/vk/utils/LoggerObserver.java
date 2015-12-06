package com.nethergrim.vk.utils;

import android.util.Log;

import com.nethergrim.vk.models.WebResponse;

import java.net.UnknownHostException;

import rx.Observer;
import rx.functions.Action1;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 05.09.15.
 */
public class LoggerObserver implements Observer<WebResponse> {

    private static LoggerObserver obs = new LoggerObserver();
    private Action1<WebResponse> mOnNextObserver;

    public LoggerObserver(Action1<WebResponse> onNextObserver) {
        mOnNextObserver = onNextObserver;
    }

    public LoggerObserver() {
    }

    public static LoggerObserver getInstance() {
        return obs;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            e.printStackTrace();
        } else {
            Log.e("WebError", e.toString() + " " + e.getMessage());
            // TODO: 05.09.15 add analytics handling here
        }
    }

    @Override
    public void onNext(WebResponse webResponse) {
        if (mOnNextObserver != null) {
            mOnNextObserver.call(webResponse);
        }

    }
}
