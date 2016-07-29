package com.nethergrim.vk.inject;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.activity.AbstractActivity;
import com.nethergrim.vk.activity.ChatActivity;
import com.nethergrim.vk.activity.MainActivity;
import com.nethergrim.vk.activity.NewChatActivity;
import com.nethergrim.vk.activity.UserProfileActivity;
import com.nethergrim.vk.adapter.ChatAdapter;
import com.nethergrim.vk.adapter.ConversationsAdapter;
import com.nethergrim.vk.adapter.FriendsAdapter;
import com.nethergrim.vk.adapter.StickerAdapter;
import com.nethergrim.vk.data.RealmStore;
import com.nethergrim.vk.emoji.EmojiPagerAdapter;
import com.nethergrim.vk.emoji.EmojiconsPopup;
import com.nethergrim.vk.emoji.StickersLayoutView;
import com.nethergrim.vk.fragment.ChatFragment;
import com.nethergrim.vk.fragment.ConversationsFragment;
import com.nethergrim.vk.fragment.FriendsFragment;
import com.nethergrim.vk.gcm.MyGcmListenerService;
import com.nethergrim.vk.gcm.MyInstanceIDListenerService;
import com.nethergrim.vk.images.PaletteProviderImpl;
import com.nethergrim.vk.services.GcmNetworkService;
import com.nethergrim.vk.services.WorkerService;
import com.nethergrim.vk.utils.DataHelper;
import com.nethergrim.vk.utils.PushParserImpl;
import com.nethergrim.vk.utils.RealmUserProviderImplementation;
import com.nethergrim.vk.views.KeyboardDetectorRelativeLayout;
import com.nethergrim.vk.views.imageViews.UserImageView;
import com.nethergrim.vk.web.DataManagerImpl;
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

    void inject(ConversationsFragment mf);

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

    void inject(DataManagerImpl d);

    void inject(RealmStore r);

    void inject(NewChatActivity newChatActivity);

    void inject(KeyboardDetectorRelativeLayout keyboardDetectorRelativeLayout);

    void inject(AbstractActivity abstractActivity);

    void inject(EmojiPagerAdapter emojiPagerAdapter);

    void inject(EmojiconsPopup emojiconsPopup);

    void inject(StickerAdapter stickerAdapter);

    void inject(UserImageView userImageView);

    void inject(StickersLayoutView stickersLayoutView);

    void inject(GcmNetworkService gcmNetworkService);
}
