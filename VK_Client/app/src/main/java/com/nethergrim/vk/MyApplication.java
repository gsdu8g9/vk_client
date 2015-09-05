package com.nethergrim.vk;

import android.app.Application;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kisstools.KissTools;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.inject.DaggerMainComponent;
import com.nethergrim.vk.inject.MainComponent;
import com.nethergrim.vk.inject.ProviderModule;
import com.nethergrim.vk.utils.UserUtils;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author andreydrobyazko on 3/20/15.
 */
public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getName();
    private static MyApplication _app;
    @Inject
    Prefs mPrefs;
    @Inject
    ImageLoader mImageLoader;
    private MainComponent mMainComponent;

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public synchronized static MyApplication getInstance() {
        return _app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KissTools.setContext(this);
        _app = this;
        Constants.mDensity = getResources().getDisplayMetrics().density;

        Log.e("FIELDS", UserUtils.getDefaultUserFieldsAsString());

        VKSdkListener vkSdkListener = new VKSdkListener() {
            @Override
            public void onCaptchaError(VKError captchaError) {
                Log.e(TAG, "captcha error: " + captchaError.toString());
            }

            @Override
            public void onTokenExpired(VKAccessToken expiredToken) {
                Log.e(TAG, "token expired: " + expiredToken.email);
            }

            @Override
            public void onAccessDenied(VKError authorizationError) {
                Log.e(TAG, "access denied (auth error) " + authorizationError.toString());
            }

            @Override
            public void onReceiveNewToken(VKAccessToken newToken) {
                super.onReceiveNewToken(newToken);
                mPrefs.setToken(newToken.accessToken);
                Log.d(TAG, "received new token");
            }

            @Override
            public void onAcceptUserToken(VKAccessToken token) {
                super.onAcceptUserToken(token);
                mPrefs.setToken(token.accessToken);
                Log.d(TAG, "user token accepted");
            }

            @Override
            public void onRenewAccessToken(VKAccessToken token) {
                super.onRenewAccessToken(token);
                mPrefs.setToken(token.accessToken);
                Log.d(TAG, "onRenewAccessToken");
            }
        };
        VKSdk.initialize(vkSdkListener, Constants.VK_APP_ID);
//        printFingerPrints();
        initDagger2();
        initRealm();
        initFresco();
    }

    public MainComponent getMainComponent() {
        return mMainComponent;
    }

    public Prefs getPrefs() {
        return mPrefs;
    }

    private void initFresco() {
        Fresco.initialize(this);
    }

    private void printFingerPrints() {
        String[] fingerprints = VKUtil.getCertificateFingerprint(this,
                this.getPackageName());
        if (fingerprints != null && BuildConfig.DEBUG) {
            for (String fingerprint : fingerprints) {
                Log.d("TAG", "key fingerprint: " + fingerprint);
            }
        }
    }

    private void initDagger2() {
        mMainComponent = DaggerMainComponent.builder().providerModule(new ProviderModule()).build();
        mMainComponent.inject(this);
    }

    private void initRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("vk_main_realm")
                .schemaVersion(1)
                .migration((realm, version) -> {
                    Log.e("TAG","migrating to " + version);
                    return 0;
                })
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
