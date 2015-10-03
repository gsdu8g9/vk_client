package com.nethergrim.vk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.nethergrim.vk.Constants;

import java.util.ArrayList;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 03.10.15.
 */
public class KeyboardDetectorRelativeLayout extends RelativeLayout {

    public static final String TAG = KeyboardDetectorRelativeLayout.class.getSimpleName();
    private ArrayList<IKeyboardChanged> mKeyboardListenersArray = new ArrayList<>();
    private int mKeyboardHeight = (int) (200 * Constants.mDensity);

    public interface IKeyboardChanged {

        void onKeyboardShown();

        void onKeyboardHidden();
    }

    public KeyboardDetectorRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KeyboardDetectorRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardDetectorRelativeLayout(Context context) {
        super(context);
    }

    public void addKeyboardStateChangedListener(IKeyboardChanged listener) {
        mKeyboardListenersArray.add(listener);
    }

    public void removeKeyboardStateChangedListener(IKeyboardChanged listener) {
        mKeyboardListenersArray.remove(listener);
    }

    public int getKeyboardHeight() {
        return mKeyboardHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();

        if (proposedheight > 0 && actualHeight > 0 && proposedheight < actualHeight) {
            mKeyboardHeight = actualHeight - proposedheight;
            Log.d("TAG", "keyboard height: " + mKeyboardHeight + " px or " + (mKeyboardHeight
                    / Constants.mDensity) + " dp");
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
