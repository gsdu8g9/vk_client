package com.nethergrim.vk.inject;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.json.JsonDeserializer;
import com.nethergrim.vk.json.JsonDeserializerImpl;
import com.nethergrim.vk.web.WebRequestManager;
import com.nethergrim.vk.web.WebRequestManagerImpl;
import com.nethergrim.vk.web.images.GlideImageLoaderImpl;
import com.nethergrim.vk.web.images.ImageLoader;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author andreydrobyazko on 4/3/15.
 */
@Module
public class ProviderModule {

    @Provides @Singleton
    WebRequestManager provideWebRequestManager(){
        return new WebRequestManagerImpl();
    }

    @Provides @Singleton
    JsonDeserializer provideJsonDeserializer(){
        return new JsonDeserializerImpl();
    }

    @Provides @Singleton
    ImageLoader provideImageLoader(){
        return new GlideImageLoaderImpl(MyApplication.getInstance());
    }
}
