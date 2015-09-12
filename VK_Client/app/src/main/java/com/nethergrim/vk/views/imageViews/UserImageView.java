package com.nethergrim.vk.views.imageViews;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nethergrim.vk.R;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserUtils;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 05.09.15.
 */
public class UserImageView extends SimpleDraweeView {

    private boolean mRoundAsCircle = true;

    public UserImageView(Context context,
            GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public UserImageView(Context context) {
        super(context);
        init();
    }

    public UserImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void display(User user, boolean roundAsCircle) {
        this.mRoundAsCircle = roundAsCircle;
        init();
        if (user != null) {
            display(UserUtils.getStablePhotoUrl(user));
        }
    }

    public void displayGroupChat() {
        mRoundAsCircle = false;
        init();
        displayRes(R.drawable.ic_social_people_outline);
    }

    protected void display(String url) {
        setImageURI(Uri.parse(url));
    }

    private void displayRes(@DrawableRes int resId) {
        setImageURI(null);
        getHierarchy().setPlaceholderImage(resId);
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }

        setHierarchy(getDefaultHierarchy());
    }

    private GenericDraweeHierarchy getDefaultHierarchy() {
        GenericDraweeHierarchy tmp = getHierarchy();
        if (tmp == null) {
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(getResources());
            tmp = builder.build();
        } else {
            tmp.reset();
        }
        tmp.setRoundingParams(new RoundingParams().setRoundAsCircle(mRoundAsCircle));
        tmp.setFadeDuration(100);
        tmp.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        return tmp;
    }
}
