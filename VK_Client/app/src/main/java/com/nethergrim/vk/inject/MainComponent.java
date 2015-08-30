package com.nethergrim.vk.inject;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.activity.ChatActivity;
import com.nethergrim.vk.activity.MainActivity;
import com.nethergrim.vk.activity.UserProfileActivity;
import com.nethergrim.vk.adapter.ChatAdapter;
import com.nethergrim.vk.adapter.ConversationsAdapter;
import com.nethergrim.vk.adapter.FriendsAdapter;
import com.nethergrim.vk.fragment.ChatFragment;
import com.nethergrim.vk.fragment.FriendsFragment;
import com.nethergrim.vk.fragment.MessagesFragment;
import com.nethergrim.vk.gcm.MyGcmListenerService;
import com.nethergrim.vk.gcm.MyInstanceIDListenerService;
import com.nethergrim.vk.images.PaletteProviderImpl;
import com.nethergrim.vk.services.WorkerService;
import com.nethergrim.vk.utils.DataHelper;
import com.nethergrim.vk.utils.PushParserImpl;
import com.nethergrim.vk.utils.RealmUserProviderImplementation;
import com.nethergrim.vk.web.WebIntentHandlerImpl;
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

    void inject(ConversationsAdapter o);

    void inject(MainActivity m);

    void inject(MyApplication m);

    void inject(RealmUserProviderImplementation obj);

    void inject(MyInstanceIDListenerService o);

    void inject(MyGcmListenerService o);

    void inject(PushParserImpl o);

    void inject(WebIntentHandlerImpl o);

    void inject(FriendsAdapter o);

    void inject(FriendsFragment o);

    void inject(UserProfileActivity a);

    void inject(PaletteProviderImpl p);

    void inject(ChatActivity a);

    void inject(ChatFragment c);

    void inject(ChatAdapter c);

    void inject(WebRequestManagerImpl r);

    void inject(WorkerService w);

    void inject(DataHelper m);
}
