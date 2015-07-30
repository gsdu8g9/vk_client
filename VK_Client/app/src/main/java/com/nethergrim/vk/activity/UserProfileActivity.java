package com.nethergrim.vk.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
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
    @InjectView(R.id.backgroundLayout)
    FrameLayout mBackgroundLayout;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.layout_toolbar)
    View mToolbarLayout;
    @InjectView(R.id.topLayout)
    FrameLayout mTopLayout;
    @InjectView(R.id.bottomLayout)
    View mBottomLayout;

    private User mUser;


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
            runBackgroundRippleAnimation(false, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mExitActivityTransition.exit(UserProfileActivity.this);
                }
            });

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
        findUser();
        initToolBar();
        startFirstAnimation(savedInstanceState);
    }

    private void findUser() {
        mUser = mRealm.where(User.class).equalTo("id", userId).findFirst();
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setTitle(mUser.getFirstName() + " " + mUser.getLastName());
    }

    private void startFirstAnimation(Bundle savedInstanceState) {
        if (mUser != null) {
            mToolbarLayout.setAlpha(0f);
            mImageLoader.displayImage(UserUtils.getStablePhotoUrl(mUser), mAvatarImageView);
            mExitActivityTransition = ActivityTransition.with(getIntent())
                    .to(mAvatarImageView)
                    .duration(ANIMATION_DURATION)
                    .start(savedInstanceState);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runBackgroundRippleAnimation(true, null);
                }
            }, ANIMATION_DURATION);
        }
    }

    private void runBackgroundRippleAnimation(boolean in,
            @Nullable Animator.AnimatorListener animationListener) {

        Drawable drawable = getResources().getDrawable(R.drawable.white_dot);
        mBackgroundAvatar.setVisibility(View.VISIBLE);

        if (in) {
            UserPalette userPalette = mImageLoader.getUserPalette(userId);
            if (userPalette != null && userPalette.getVibrant() != 0) {
                drawable.setColorFilter(userPalette.getVibrantLight(), PorterDuff.Mode.MULTIPLY);
            } else {
                drawable.setColorFilter(getResources().getColor(R.color.primary_light),
                        PorterDuff.Mode.MULTIPLY);
            }
            mBackgroundAvatar.setBackgroundDrawable(drawable);
            mBackgroundAvatar.animate().setDuration(ANIMATION_DURATION).scaleX(3f).scaleY(
                    3f).start();
            mBottomLayout.setVisibility(View.VISIBLE);
            mBottomLayout.setTranslationY(1000f);
            mBottomLayout.animate().translationY(0f).setDuration(ANIMATION_DURATION).start();
        } else {
            mBackgroundAvatar.animate().setDuration(ANIMATION_DURATION).scaleX(1f).alpha(0f).scaleY(
                    1f).start();
            mBottomLayout.setTranslationY(0f);
            mBottomLayout.animate()
                    .translationY(1000f)
                    .alpha(0f)
                    .setDuration(ANIMATION_DURATION)
                    .start();

        }
        mToolbarLayout.setAlpha(in ? 0f : 1f);
        mToolbarLayout.animate().setDuration(ANIMATION_DURATION).alpha(in ? 1f : 0f).start();
        mShadowLayout.animate()
                .setDuration(ANIMATION_DURATION)
                .alpha(in ? 1f : 0f)
                .setListener(animationListener)
                .start();

    }

}
