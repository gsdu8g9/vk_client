package com.nethergrim.vk.inject;

import android.content.Context;
import android.os.Process;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.DefaultPrefsImpl;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.data.RealmStore;
import com.nethergrim.vk.data.Store;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.images.ImageLoaderImpl;
import com.nethergrim.vk.images.PaletteProvider;
import com.nethergrim.vk.images.PaletteProviderImpl;
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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 4/3/15.
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
    Store providePersistingManager() {
        return new RealmStore();
    }

    @Provides
    @Singleton
    Executor provideSingleThreadExecutor() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Thread.currentThread().setName("Messaging thread");
        });
        return executor;
    }

    @Provides
    @Singleton
    Scheduler provideSingleThreadScheduler(Executor executor) {
        return Schedulers.from(executor);
    }



}
