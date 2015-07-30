package com.nethergrim.vk.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.dd.ShadowLayout;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.models.UserPalette;
import com.nethergrim.vk.utils.UserUtils;
import com.nethergrim.vk.web.images.ImageLoader;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class UserProfileActivity extends AbstractActivity {

    public static final String BUNDLE_EXTRA_USER_ID = "user_id";
    public static final int ANIMATION_DURATION = 300;
    @InjectView(R.id.imageView2)
    ImageView mAvatarImageView;

    @Inject
    Realm mRealm;
    @Inject
    ImageLoader mImageLoader;
    @InjectView(R.id.backgroundAvatar)
    View mBackgroundAvatar;
    @InjectView(R.id.shadow_layout)
    ShadowLayout mShadowLayout;

    private ExitActivityTransition mExitActivityTransition;


    private long userId;

    public static void show(long userId, Activity activity, ImageView avatarToTransit) {
        final Intent intent = new Intent(activity, UserProfileActivity.class);
        intent.putExtra(BUNDLE_EXTRA_USER_ID, userId);
        ActivityTransitionLauncher.with(activity)
                .from(avatarToTransit)
                .launch(intent);
    }

    @Override
    public void onBackPressed() {
        if (mExitActivityTransition != null) {
            mExitActivityTransition.exit(this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.inject(this);
        MyApplication.getInstance().getMainComponent().inject(this);

        userId = getIntent().getExtras().getLong(BUNDLE_EXTRA_USER_ID, 0);
        User user = mRealm.where(User.class).equalTo("id", userId).findFirst();
        if (user != null) {
            mImageLoader.displayImage(UserUtils.getStablePhotoUrl(user), mAvatarImageView);
            mExitActivityTransition = ActivityTransition.with(getIntent())
                    .to(mAvatarImageView)
                    .duration(ANIMATION_DURATION)
                    .start(savedInstanceState);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runBackgroundRippleAnimation();
                }
            }, ANIMATION_DURATION);
        }

    }

    private void runBackgroundRippleAnimation() {
        UserPalette userPalette = mImageLoader.getUserPalette(userId);

        Drawable drawable = getResources().getDrawable(R.drawable.white_dot);
        if (userPalette != null && userPalette.getVibrant() != 0) {
            drawable.setColorFilter(userPalette.getVibrantLight(), PorterDuff.Mode.MULTIPLY);
        } else {
            drawable.setColorFilter(getResources().getColor(R.color.primary_light),
                    PorterDuff.Mode.MULTIPLY);
        }
        mBackgroundAvatar.setBackgroundDrawable(drawable);
        mBackgroundAvatar.animate().setDuration(ANIMATION_DURATION).scaleX(3f).scaleY(3f).start();
        mShadowLayout.animate().setDuration(ANIMATION_DURATION).alpha(1f).start();
    }

}
