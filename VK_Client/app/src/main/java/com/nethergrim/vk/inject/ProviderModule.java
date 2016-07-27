package com.nethergrim.vk.inject;

import android.content.Context;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.DefaultPrefsImpl;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.data.PersistingManager;
import com.nethergrim.vk.data.RealmPersistingManagerImpl;
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
import com.nethergrim.vk.web.WebIntentHandler;
import com.nethergrim.vk.web.WebIntentHandlerImpl;
import com.nethergrim.vk.web.WebRequestManager;
import com.nethergrim.vk.web.WebRequestManagerImpl;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author andreydrobyazko on 4/3/15.
 */
@Module
public class ProviderModule {

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
    PersistingManager providePersistingManager() {
        return new RealmPersistingManagerImpl();
    }

}
