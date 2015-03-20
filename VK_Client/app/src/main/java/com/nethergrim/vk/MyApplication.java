package com.nethergrim.vk;

import android.app.Application;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdkListener vkSdkListener = new VKSdkListener() {
            @Override
            public void onCaptchaError(VKError captchaError) {
                Log.e("TAG", "captcha error: " + captchaError.errorMessage);
            }

            @Override
            public void onTokenExpired(VKAccessToken expiredToken) {
                Log.e("TAG", "token expired: " + expiredToken.email);
            }

            @Override
            public void onAccessDenied(VKError authorizationError) {
                Log.e("TAG", "access denied (auth error) " + authorizationError.errorMessage);
            }
        };
        VKSdk.initialize(vkSdkListener, Constants.VK_APP_ID);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        for (String fingerprint : fingerprints) {
            Log.e("TAG", "key fingerprint: " + fingerprint);
        }
    }

}
