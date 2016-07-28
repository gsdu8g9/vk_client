package com.nethergrim.vk.views.imageViews;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserUtils;

import javax.inject.Inject;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 05.09.15.
 */

@SuppressWarnings("WeakerAccess")
public class UserImageView extends ImageView {

    @Inject
    ImageLoader imageLoader;

    public UserImageView(Context context) {
        super(context);
        init();
    }

    public UserImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void display(User user, boolean roundAsCircle) {
        if (user == null) {
            return;
        }
        if (roundAsCircle) {
            imageLoader.loadCircleImage(UserUtils.getStablePhotoUrl(user), this);
        } else {
            imageLoader.loadImage(UserUtils.getStablePhotoUrl(user), this);
        }
    }

    public void displayGroupChat() {
        displayRes(R.drawable.ic_social_people_outline);
    }

    protected void display(String url) {
        imageLoader.loadImage(url, this);
    }

    private void displayRes(@DrawableRes int resId) {
        this.setImageResource(resId);
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        MyApplication.getInstance().getMainComponent().inject(this);
    }

}
