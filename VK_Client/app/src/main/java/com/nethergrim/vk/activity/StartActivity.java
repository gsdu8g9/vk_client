package com.nethergrim.vk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.R;
import com.vk.sdk.VKSdk;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class StartActivity extends AbstractActivity {

    @InjectView(R.id.image)
    ImageView mImage;
    @InjectView(R.id.btn_sign_in)
    Button mBtnSignIn;
    private boolean mIsLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSession();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!mIsLoggedIn){
            mImage.animate().scaleX(1f).scaleY(1f).setStartDelay(30).rotation(360).setDuration(1000).setInterpolator(new AnticipateOvershootInterpolator()).start();
        }
    }

    private void checkSession() {
        mIsLoggedIn = VKSdk.wakeUpSession(this);
        if (mIsLoggedIn) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @OnClick(R.id.btn_sign_in)
    public void signIn(View v) {
        VKSdk.authorize(Constants.PERMISSIONS, false, true);
    }
}
