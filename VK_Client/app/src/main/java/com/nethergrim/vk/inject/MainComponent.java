package com.nethergrim.vk.inject;

import com.nethergrim.vk.activity.MainActivity;
import com.nethergrim.vk.adapter.ConversationsAdapter;
import com.nethergrim.vk.fragment.MessagesFragment;
import com.nethergrim.vk.web.WebRequestManagerImpl;

import javax.inject.Singleton;

import dagger.Component;

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
    void inject(MessagesFragment mf);

    void inject(WebRequestManagerImpl o);

    void inject(ConversationsAdapter o);

    void inject(MainActivity m);
}
