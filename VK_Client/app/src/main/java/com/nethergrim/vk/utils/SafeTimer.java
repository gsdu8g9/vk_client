package com.nethergrim.vk.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class SafeTimer extends Handler {

    private Runnable mRunnable;
    private int mDelayMS;
    private Runnable mMyRunnable;

    public SafeTimer(Runnable runnable, int delayInSec) {
        super(Looper.getMainLooper());
        mRunnable = runnable;
        mDelayMS = delayInSec * 1000;
    }

    public void setDelayMS(int ms) {
        this.mDelayMS = ms;
    }

    /**
     * Should be called in activity/fragment onResume method
     */
    public void start() {
        if (mMyRunnable == null) {
            mMyRunnable = new Runnable() {
                @Override
                public void run() {
                    mRunnable.run();
                    postDelayed(this, mDelayMS);
                }
            };
        }
        post(mMyRunnable);
    }

    /**
     * Should be called in activity/fragment onPause method to avoid leaks
     */
    public void finish() {
        if (mMyRunnable != null) {
            removeCallbacks(mMyRunnable);
        }
    }
}
