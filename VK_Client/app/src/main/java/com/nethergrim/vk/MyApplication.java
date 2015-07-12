package com.nethergrim.vk;

import android.app.Application;
import android.util.Log;
import com.kisstools.KissTools;
import com.nethergrim.vk.inject.DaggerMainComponent;
import com.nethergrim.vk.inject.MainComponent;
import com.nethergrim.vk.inject.ProviderModule;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class MyApplication extends Application {

    private static MyApplication _app;

    public synchronized static MyApplication getInstance() {
        return _app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KissTools.setContext(this);
        _app = this;
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

            @Override
            public void onReceiveNewToken(VKAccessToken newToken) {
                super.onReceiveNewToken(newToken);
                Log.e("TAG", "received new token: " + newToken.email);
            }

            @Override
            public void onAcceptUserToken(VKAccessToken token) {
                super.onAcceptUserToken(token);
                Log.e("TAG", "user token accepted: " + token.email);
            }

            @Override
            public void onRenewAccessToken(VKAccessToken token) {
                super.onRenewAccessToken(token);
                Log.e("TAG", "onRenewAccessToken: " + token.email);
            }
        };
        VKSdk.initialize(vkSdkListener, Constants.VK_APP_ID);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        for (String fingerprint : fingerprints) {
            Log.e("TAG", "key fingerprint: " + fingerprint);
        }

        initDagger2();
    }

    private void initDagger2() {
        MainComponent mainComponent = DaggerMainComponent.builder().providerModule(new ProviderModule()).build();

    }

}
