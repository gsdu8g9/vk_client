package com.nethergrim.vk.data;

import android.support.annotation.NonNull;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.event.ConversationsUpdatedEvent;
import com.nethergrim.vk.event.FriendsUpdatedEvent;
import com.nethergrim.vk.event.MyUserUpdatedEvent;
import com.nethergrim.vk.event.UsersUpdatedEvent;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.images.PaletteProvider;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ConversationsUserObject;
import com.nethergrim.vk.models.ListOfFriends;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.Message;
import com.nethergrim.vk.models.PendingMessage;
import com.nethergrim.vk.models.StartupResponse;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.DataHelper;
import com.squareup.otto.Bus;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 30.08.15.
 */
public class RealmStore implements Store {

    @Inject
    Prefs mPrefs;

    @Inject
    Bus mBus;

    @Inject
    PaletteProvider mPaletteProvider;

    @Inject
    ImageLoader mImageLoader;

    @Inject
    Executor executor;

    private Realm messagingRealm;

    public RealmStore() {
        MyApplication.getInstance().getMainComponent().inject(this);
        executor.execute(() -> messagingRealm = Realm.getDefaultInstance());
    }

    @Override
    public void persist(@NonNull StartupResponse startupResponse) {
        mPrefs.setCurrentUserId(startupResponse.getResponse().getMe().getId());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(startupResponse.getResponse().getMe());
        realm.commitTransaction();
        mBus.post(new MyUserUpdatedEvent());
    }

    @Override
    public void persist(@NonNull ListOfFriends listOfFriends, int offset) {
        mPrefs.setFriendsCount(listOfFriends.getResponse().getCount());

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<User> friends = listOfFriends.getResponse().getFriends();
        mPaletteProvider.generateAndStorePalette(friends);
        for (int i = 0, size = friends.size(), rating = offset;
             i < size;
             i++, rating++) {
            friends.get(i).setFriendRating(rating);
            // TODO: 30.08.15 fix friends rating persistense, to make it consistent.
        }
        realm.copyToRealmOrUpdate(friends);
        realm.commitTransaction();

        mBus.post(new FriendsUpdatedEvent(listOfFriends.getResponse().getCount()));
    }

    @Override
    public void persist(ListOfUsers listOfUsers) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(listOfUsers.getResponse());
        realm.commitTransaction();
        mPaletteProvider.generateAndStorePalette(listOfUsers.getResponse());
        mBus.post(new UsersUpdatedEvent());
    }

    @Override
    public void persist(ConversationsUserObject conversationsUserObject,
                        boolean clearDataBeforePersist) {
        //saving conversations to db
        ConversationsList conversationsList
                = conversationsUserObject.getResponse().getConversations();
        conversationsList.setResults(
                DataHelper.normalizeConversationsList(conversationsList.getResults()));
        mPrefs.setUnreadMessagesCount(conversationsList.getUnreadCount());
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        if (clearDataBeforePersist) {
            realm.delete(Conversation.class);
        }

        realm.copyToRealmOrUpdate(conversationsList.getResults());

        //saving users to db
        List<User> users = conversationsUserObject.getResponse().getUsers();
        realm.copyToRealmOrUpdate(users);

        realm.commitTransaction();
        realm.close();
        mBus.post(new ConversationsUpdatedEvent());
        mBus.post(new UsersUpdatedEvent());
    }

    @Override
    public void persist(ListOfMessages listOfMessages) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(listOfMessages.getMessages());
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void deleteConversation(long userId, long chatId) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        boolean isGroupChat = chatId > 0;
        long convId = isGroupChat ? chatId : userId;
        Conversation conversation = realm.where(Conversation.class)
                .equalTo("id", convId)
                .findFirst();
        RealmQuery<Message> messageRealmQuery = realm.where(Message.class);
        if (isGroupChat) {
            messageRealmQuery = messageRealmQuery.equalTo("chat_id", convId);
        } else {
            messageRealmQuery = messageRealmQuery.equalTo("user_id", convId);
        }
        RealmResults<Message> messages = messageRealmQuery.findAll();


        for (int i = messages.size() - 1; i >= 0; i--) {
            RealmObject.deleteFromRealm(messages.get(i));

        }
        RealmObject.deleteFromRealm(conversation);

        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void markMessagesAsRead(long convId, long lastReadMessage) {
        boolean isGroupChat = false;
        if (convId > 2000000000L) {
            convId = convId - 2000000000L;
            isGroupChat = true;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<Message> messageRealmQuery = realm.where(Message.class);
        if (isGroupChat) {
            messageRealmQuery = messageRealmQuery.equalTo("chat_id", convId).equalTo("out", 0);
        } else {
            messageRealmQuery = messageRealmQuery.equalTo("user_id", convId).equalTo("out", 0);
        }
        RealmResults<Message> messagesToBeMarkedAsRead = messageRealmQuery.lessThanOrEqualTo("id", lastReadMessage).findAllSorted("date", Sort.ASCENDING);
        for (int i = 0, messagesToBeMarkedAsReadSize = messagesToBeMarkedAsRead.size(); i < messagesToBeMarkedAsReadSize; i++) {
            Message message = messagesToBeMarkedAsRead.get(i);
            message.setRead_state(1);
        }
        realm.commitTransaction();
        realm.close();
    }


    @Override
    @DebugLog
    public synchronized void savePendingMessage(long peerId, @NonNull PendingMessage pendingMessage) {
        executor.execute(() -> {
            if (messagingRealm == null) {
                messagingRealm = Realm.getDefaultInstance();
            }
            messagingRealm.beginTransaction();
            Message message = pendingMessage.mapToMessage(mPrefs.getCurrentUserId());
            long id = message.getConversationId();

            message = messagingRealm.copyToRealmOrUpdate(message);

            Conversation conversation = messagingRealm.where(Conversation.class).equalTo("id", id).findFirst();
            conversation.setMessage(message);
            conversation.setDate(message.getDate());

            messagingRealm.copyToRealmOrUpdate(conversation);
            mBus.post(new ConversationsUpdatedEvent());
            messagingRealm.commitTransaction();
        });

    }

    @Override
    @DebugLog
    public List<PendingMessage> getUnsentMessages() {
        RealmResults<Message> unsentMessages = Realm.getDefaultInstance().where(Message.class).equalTo("pending", true).findAllSorted("date", Sort.ASCENDING);
        List<Message> result = messagingRealm.copyFromRealm(unsentMessages);
        return PendingMessage.Companion.fromMessages(result);
    }

    @Override
    @DebugLog
    public synchronized void removePendingMessage(long peerId, long randomId, ListOfMessages listOfMessages) {
        executor.execute(() -> {
            if (messagingRealm == null) {
                messagingRealm = Realm.getDefaultInstance();
            }
            messagingRealm.beginTransaction();
            RealmObject.deleteFromRealm(messagingRealm.where(Message.class).equalTo("id", randomId).findFirst());
            messagingRealm.copyToRealmOrUpdate(listOfMessages.getMessages());
            messagingRealm.commitTransaction();
        });

    }
}
