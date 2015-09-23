package com.nethergrim.vk.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;


/**
 * @author andrej on 18.08.15.
 */
public class AndroidBus extends Bus {

    private Handler mMainThreadHandler;

    public AndroidBus() {
        super(ThreadEnforcer.ANY);
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(final Object event) {
        if (isInMainThread()) {
            super.post(event);
        } else {
            mMainThreadHandler.post(() -> post(event));
        }
    }

    private boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
