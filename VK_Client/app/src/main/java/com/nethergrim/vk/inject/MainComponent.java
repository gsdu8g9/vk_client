package com.nethergrim.vk.inject;

import dagger.Component;

import javax.inject.Singleton;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
@Singleton
@Component(
        modules = {
                ProviderModule.class
        }
)

public interface MainComponent {
    void inject(Class clazz);
}
