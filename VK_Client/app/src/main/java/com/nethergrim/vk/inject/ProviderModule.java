package com.nethergrim.vk.inject;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.DefaultPrefsImpl;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.images.PaletteProvider;
import com.nethergrim.vk.images.PaletteProviderImpl;
import com.nethergrim.vk.images.PicassoImageLoaderImpl;
import com.nethergrim.vk.json.JacksonJsonDeserializerImpl;
import com.nethergrim.vk.json.JsonDeserializer;
import com.nethergrim.vk.utils.AndroidBus;
import com.nethergrim.vk.utils.PushParser;
import com.nethergrim.vk.utils.PushParserImpl;
import com.nethergrim.vk.utils.RealmUserProviderImplementation;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.web.DataManager;
import com.nethergrim.vk.web.RealmDataManagerImpl;
import com.nethergrim.vk.web.RetrofitRequestManagerImpl;
import com.nethergrim.vk.web.WebRequestManager;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * @author andreydrobyazko on 4/3/15.
 */
@Module
public class ProviderModule {

    @Provides
    @Singleton
    WebRequestManager provideWebRequestManager() {
        return new RetrofitRequestManagerImpl();
    }

    @Provides
    @Singleton
    JsonDeserializer provideJsonDeserializer() {
        return new JacksonJsonDeserializerImpl();
    }

    @Provides
    @Singleton
    ImageLoader provideImageLoader() {
        return new PicassoImageLoaderImpl(MyApplication.getInstance());
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
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new AndroidBus();
    }

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return new RealmDataManagerImpl();
    }

    @Provides
    @Singleton
    PaletteProvider providePaletteProvider() {
        return new PaletteProviderImpl();
    }

}
