package com.nethergrim.vk;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.inject.DaggerMainComponent;
import com.nethergrim.vk.inject.MainComponent;
import com.nethergrim.vk.inject.ProviderModule;
import com.nethergrim.vk.services.GcmNetworkService;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 3/20/15.
 */
public class MyApplication extends MultiDexApplication {

    public static final String TAG = MyApplication.class.getName();
    private static MyApplication _app;
    @Inject
    Prefs mPrefs;

    private MainComponent mMainComponent;


    public synchronized static MyApplication getInstance() {
        return _app;
    }

    @Override
    @DebugLog
    public void onCreate() {
        super.onCreate();
        _app = this;
        ViewTarget.setTagId(R.id.glide_tag);
        Constants.mDensity = getResources().getDisplayMetrics().density;

//        Log.e("FIELDS", UserUtils.getDefaultUserFieldsAsString());

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
//        logFingerPrints();
        initDagger2();
        initRealm();
        scheduleGcmNetworkManager();
    }

    /**
     * Will schedule triggering of GcmNetworkManager that will launch {@link com.nethergrim.vk.services.GcmNetworkService}
     */
    public void scheduleGcmNetworkManager() {
        PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(GcmNetworkService.class)
                .setPersisted(true)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setPeriod(3 * 60 * 60) // at most every 3 hours
                .setFlex(600)
                .setTag(GcmNetworkService.class.getSimpleName())
                .build();
        GcmNetworkManager.getInstance(this).schedule(periodicTask);
    }

    public MainComponent getMainComponent() {
        return mMainComponent;
    }

    public Prefs getPrefs() {
        return mPrefs;
    }

    private void logFingerPrints() {
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
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
