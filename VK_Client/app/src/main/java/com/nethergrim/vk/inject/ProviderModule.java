package com.nethergrim.vk.inject;

import com.nethergrim.vk.fragment.MessagesFragment;
import com.nethergrim.vk.json.JsonDeserializer;
import com.nethergrim.vk.json.JsonDeserializerImpl;
import com.nethergrim.vk.web.WebRequestManager;
import com.nethergrim.vk.web.WebRequestManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author andreydrobyazko on 4/3/15.
 */
@Module( injects = {MessagesFragment.class, WebRequestManagerImpl.class})
public class ProviderModule {

    @Provides @Singleton
    WebRequestManager provideWebRequestManager(){
        return new WebRequestManagerImpl();
    }

    @Provides @Singleton
    JsonDeserializer provideJsonDeserializer(){
        return new JsonDeserializerImpl();
    }
}
