package com.nethergrim.vk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 03.10.15.
 */
public class KeyboardDetectorRelativeLayout extends RelativeLayout {

    public static final String TAG = KeyboardDetectorRelativeLayout.class.getSimpleName();
    @Inject
    Prefs mPrefs;
    private ArrayList<IKeyboardChanged> mKeyboardListenersArray = new ArrayList<>();
    private int mKeyboardHeight = 0;

    public interface IKeyboardChanged {

        void onKeyboardShown();

        void onKeyboardHidden();
    }

    public KeyboardDetectorRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    public KeyboardDetectorRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    public KeyboardDetectorRelativeLayout(Context context) {
        super(context);
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    public void addKeyboardStateChangedListener(IKeyboardChanged listener) {
        mKeyboardListenersArray.add(listener);
    }

    public void removeKeyboardStateChangedListener(IKeyboardChanged listener) {
        mKeyboardListenersArray.remove(listener);
    }

    public int getKeyboardHeight() {
        if (mKeyboardHeight > 0) {
            return mKeyboardHeight;
        }
        int height = mPrefs.getKeyboardHeight();
        if (height > 0) {
            return height;
        }
        return (int) (220 * Constants.mDensity);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();

        if (proposedheight > 0 && actualHeight > 0 && proposedheight < actualHeight) {
            mKeyboardHeight = actualHeight - proposedheight;
            if (mKeyboardHeight > 100 * Constants.mDensity) {
                mPrefs.setKeyboardHeight(mKeyboardHeight);
            }

        }

        if (actualHeight > proposedheight) {
            notifyKeyboardShown();
        } else if (actualHeight < proposedheight) {
            notifyKeyboardHidden();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void notifyKeyboardHidden() {
        for (IKeyboardChanged listener : mKeyboardListenersArray) {
            listener.onKeyboardHidden();
        }
    }

    private void notifyKeyboardShown() {
        for (IKeyboardChanged listener : mKeyboardListenersArray) {
            listener.onKeyboardShown();
        }
    }

}
