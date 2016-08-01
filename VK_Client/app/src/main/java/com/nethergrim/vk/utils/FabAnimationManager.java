package com.nethergrim.vk.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.design.widget.FloatingActionButton;

import com.nethergrim.vk.Constants;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 08.08.15.
 */
public class FabAnimationManager {

    private static final float MAX_TRANSLATION_DELTA = 82 * Constants.mDensity;
    private ValueAnimator mValueAnimator;
    private FloatingActionButton mFloatingActionButton;
    private boolean mIsHiding;
    private boolean mIsShowing;

    public FabAnimationManager(FloatingActionButton floatingActionButton) {
        mFloatingActionButton = floatingActionButton;
    }

    public void showFab() {
        if (!mIsShowing) {
            mIsHiding = false;
            mIsShowing = true;
            initValueAnimator(0f, false);
            mValueAnimator.start();
        }
    }

    public void hideFab() {
        if (!mIsHiding) {
            mIsShowing = false;
            mIsHiding = true;
            initValueAnimator(1f, true);
            mValueAnimator.start();
        }
    }

    private void initValueAnimator(float to, final boolean starting) {
        if (mValueAnimator != null) {
            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.cancel();
        }
        mValueAnimator = ValueAnimator.ofFloat(getCurrentRelatedTranslation(), to);
        mValueAnimator.setDuration(Constants.ANIMATION_DURATION);
        mValueAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();

            // value from 0f to 1f
            mFloatingActionButton.setTranslationY(getRelatedTranslation(value));

        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                if (starting) {
                    mIsHiding = false;
                } else {
                    mIsShowing = false;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (starting) {
                    mIsHiding = false;
                } else {
                    mIsShowing = false;
                }
            }
        });

    }

    private float getCurrentRelatedTranslation() {
        return mFloatingActionButton.getTranslationY() / MAX_TRANSLATION_DELTA;
    }

    private float getRelatedTranslation(float value) {
        return value * MAX_TRANSLATION_DELTA;
    }

}
