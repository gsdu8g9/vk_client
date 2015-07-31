package com.nethergrim.vk.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.nethergrim.vk.R;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class RevealContainer extends FrameLayout {

    public static final int ANIMATION_DURATION = 300;
    public static final Interpolator INTERPOLATOR = new AccelerateDecelerateInterpolator();

    private int mMainColor;
    private int mContainerHeight;
    private int mContainerWidth;
    private View mRevealView;
    private int mStartSize = 1;
    private int mMaxSize;
    private boolean mInMaxSizeNow;
    private boolean mStartedIn;
    private boolean mStartedOut;
    private int mDuration = ANIMATION_DURATION;

    public RevealContainer(Context context) {
        super(context);
        init(context);
    }

    public RevealContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RevealContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setMainColor(int mainColor) {
        mMainColor = mainColor;
        Drawable drawable = getResources().getDrawable(R.drawable.white_dot);
        if (drawable == null) {
            return;
        }
        drawable.setColorFilter(mainColor, PorterDuff.Mode.MULTIPLY);
        mRevealView.setBackgroundDrawable(drawable);
        mRevealView.setVisibility(GONE);
    }

    public void setMaxSize(int maxSize) {
        mMaxSize = maxSize;
        if (mInMaxSizeNow) {
            float scale = getResultedScaleDelta();
            mRevealView.setScaleX(scale);
            mRevealView.setScaleY(scale);
        }
    }

    public void setStartSize(int startSize) {
        mStartSize = startSize;
        if (!mInMaxSizeNow) {
            mRevealView.getLayoutParams().height = mRevealView.getLayoutParams().width = startSize;
            mRevealView.requestLayout();
        }
    }

    public void startIn() {
        mStartedOut = false;
        if (!mStartedIn) {
            mRevealView.setVisibility(VISIBLE);
            mStartedIn = true;
            if (mContainerHeight != 0 && mContainerWidth != 0) {
                mMaxSize = (int) (Math.max(mContainerHeight, mContainerWidth) * 1.41);
                float delta = getResultedScaleDelta();
                mRevealView.animate()
                        .scaleX(delta)
                        .scaleY(delta)
                        .setDuration(mDuration)
                        .setInterpolator(INTERPOLATOR)
                        .start();
                mInMaxSizeNow = true;
            }
        }
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void startOut() {
        mStartedIn = false;
        if (!mStartedOut) {
            mStartedOut = true;
            mRevealView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(mDuration)
                    .setInterpolator(INTERPOLATOR)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mRevealView.setVisibility(GONE);
                        }
                    })
                    .start();
        }
    }

    private float getResultedScaleDelta() {
        return (float) mMaxSize / (float) mStartSize;
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }

        mRevealView = new View(context);
        mRevealView.setBackgroundResource(R.drawable.white_dot);
        FrameLayout.LayoutParams params = new LayoutParams(mStartSize, mStartSize, Gravity.CENTER);

        addView(mRevealView, params);

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressWarnings("deprecation")
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= 16) {
                                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            mContainerWidth = getWidth();
                            mContainerHeight = getHeight();
                            if (mStartedIn) {
                                mStartedIn = false;
                                startIn();
                            }
                        }
                    });
        }

    }
}
