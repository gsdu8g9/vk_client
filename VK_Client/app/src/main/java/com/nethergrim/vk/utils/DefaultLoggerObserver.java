package com.nethergrim.vk.utils;

import android.util.Log;

import rx.Observer;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class DefaultLoggerObserver implements Observer<Object> {

    public static final String TAG = DefaultLoggerObserver.class.getSimpleName();
    private static DefaultLoggerObserver obs = new DefaultLoggerObserver();

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.e(TAG, e.getMessage());
    }

    @Override
    public void onNext(Object t) {

    }

    public static DefaultLoggerObserver getInstance() {
        return obs;
    }

}
