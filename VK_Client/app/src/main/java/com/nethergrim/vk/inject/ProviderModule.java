package com.nethergrim.vk.inject;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.adapter.ConversationsAdapter;
import com.nethergrim.vk.fragment.MessagesFragment;
import com.nethergrim.vk.json.JsonDeserializer;
import com.nethergrim.vk.json.JsonDeserializerImpl;
import com.nethergrim.vk.web.WebRequestManager;
import com.nethergrim.vk.web.WebRequestManagerImpl;
import com.nethergrim.vk.web.images.GlideImageLoaderImpl;
import com.nethergrim.vk.web.images.ImageLoader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author andreydrobyazko on 4/3/15.
 */
@Module( injects = {MessagesFragment.class, WebRequestManagerImpl.class, ConversationsAdapter.class})
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
