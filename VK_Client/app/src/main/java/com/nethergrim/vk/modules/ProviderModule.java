package com.nethergrim.vk.modules;

import com.nethergrim.vk.fragment.MessagesFragment;
import com.nethergrim.vk.web.WebRequestManager;
import com.nethergrim.vk.web.WebRequestManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author andreydrobyazko on 4/3/15.
 */
@Module( injects = {MessagesFragment.class})
public class ProviderModule {

    @Provides @Singleton
    WebRequestManager provideWebRequestManager(){
        return new WebRequestManagerImpl();
    }
}
