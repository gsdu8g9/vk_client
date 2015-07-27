package com.nethergrim.vk.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.R;

/**
 * @author andrej on 27.07.15.
 */
public class MenuButton extends FrameLayout {

    public static final int DOT_SIZE_DP = 8;
    public static final int DOT_MARGIN_TOP_RIGHT_DP = 12;
    private ImageButton mImageButton;
    private View mDotView;

    public MenuButton(Context context) {
        super(context);
        init(context);
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setAlert(boolean displayingAlert) {
        mDotView.setVisibility(displayingAlert ? VISIBLE : GONE);
    }

    public void setImageDrawable(@DrawableRes int id) {
        mImageButton.setImageResource(id);
    }

    public void setImageDrawable(Drawable drawable) {
        mImageButton.setImageDrawable(drawable);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        mImageButton = new ImageButton(context);
        mImageButton.setBackgroundResource(0);
        addView(mImageButton, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mDotView = new View(context);
        mDotView.setBackgroundResource(R.drawable.red_dot);
        int sizePx = (int) (DOT_SIZE_DP * Constants.mDensity);
        addView(mDotView, new FrameLayout.LayoutParams(sizePx,
                sizePx));
        int marginPx = (int) (DOT_MARGIN_TOP_RIGHT_DP * Constants.mDensity);
        FrameLayout.LayoutParams params = (LayoutParams) mDotView.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        params.setMargins(0, marginPx, marginPx, 0);
        mDotView.setLayoutParams(params);
        setAlert(false);
    }
}
