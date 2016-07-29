package com.nethergrim.vk.activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.R;
import com.vk.sdk.VKSdk;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

/**
 * Starter {@link AbstractActivity} that should launch at the app start.
 * Should display sign-in screen, or redirect to {@link MainActivity} if user is currently logged
 * in.
 *
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class StartActivity extends AbstractActivity {

    @InjectView(R.id.image)
    ImageView mImage;
    @InjectView(R.id.btn_sign_in)
    Button mBtnSignIn;
    private boolean mIsLoggedIn;

    @OnClick(R.id.btn_sign_in)
    void signIn(View v) {
        VKSdk.authorize(Constants.PERMISSIONS, false, true);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!mIsLoggedIn) {
            mImage.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setStartDelay(30)
                    .rotation(360)
                    .setDuration(1000)
                    .setInterpolator(new AnticipateOvershootInterpolator())
                    .start();
        }
    }

    @Override
    @DebugLog
    protected void onResume() {
        super.onResume();
        mIsLoggedIn = VKSdk.wakeUpSession(this);
        if (mIsLoggedIn) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_start);
            ButterKnife.inject(this);
        }
    }
}
