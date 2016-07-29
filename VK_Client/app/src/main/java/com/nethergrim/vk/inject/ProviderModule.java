package com.nethergrim.vk.inject;

import android.content.Context;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.DefaultPrefsImpl;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.data.RealmStore;
import com.nethergrim.vk.data.Store;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.images.PaletteProvider;
import com.nethergrim.vk.images.PaletteProviderImpl;
import com.nethergrim.vk.images.ImageLoaderImpl;
import com.nethergrim.vk.json.JacksonJsonDeserializerImpl;
import com.nethergrim.vk.json.JsonDeserializer;
import com.nethergrim.vk.utils.AndroidBus;
import com.nethergrim.vk.utils.PushParser;
import com.nethergrim.vk.utils.PushParserImpl;
import com.nethergrim.vk.utils.RealmUserProviderImplementation;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.web.DataManager;
import com.nethergrim.vk.web.DataManagerImpl;
import com.nethergrim.vk.web.RetrofitInterface;
import com.nethergrim.vk.web.WebIntentHandler;
import com.nethergrim.vk.web.WebIntentHandlerImpl;
import com.nethergrim.vk.web.WebRequestManager;
import com.nethergrim.vk.web.WebRequestManagerImpl;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 4/3/15.
 */
@Module
public class ProviderModule {

    private MyApplication app;

    public ProviderModule(MyApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    WebRequestManager provideWebRequestManager() {
        return new WebRequestManagerImpl();
    }

    @Provides
    @Singleton
    JsonDeserializer provideJsonDeserializer() {
        return new JacksonJsonDeserializerImpl();
    }

    @Provides
    @Singleton
    ImageLoader provideImageLoader(Context context) {
        return new ImageLoaderImpl(context);
    }

    @Provides
    @Singleton
    Prefs provideSharedPreferences() {
        return new DefaultPrefsImpl();
    }

    @Provides
    UserProvider provideUserProvider() {
        return new RealmUserProviderImplementation();
    }

    @Provides
    @Singleton
    PushParser providePushParser() {
        return new PushParserImpl();
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new AndroidBus();
    }

    @Provides
    @Singleton
    Context provideContext() {
        return MyApplication.getInstance();
    }

    @Provides
    @Singleton
    WebIntentHandler provideWebIntentHandler(Prefs prefs, Context context) {
        return new WebIntentHandlerImpl(prefs, context);
    }

    @Provides
    @Singleton
    PaletteProvider providePaletteProvider() {
        return new PaletteProviderImpl();
    }

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return new DataManagerImpl();
    }

    @Provides
    @Singleton
    Store providePersistingManager() {
        return new RealmStore();
    }

    @SuppressWarnings("deprecation")
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setRetryOnConnectionFailure(true);
        okHttpClient.setCache(new Cache(app.getCacheDir(), 1024 * 1024 * 200));
        okHttpClient.networkInterceptors().add(new StethoInterceptor());
        return okHttpClient;
    }

    @Provides
    @Singleton
    RetrofitInterface provideRetrofitInterface(OkHttpClient okHttpClient) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASIC_API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new JacksonConverter())
                .setClient(new OkClient(okHttpClient))
                .build();
        return restAdapter.create(RetrofitInterface.class);
    }

}
