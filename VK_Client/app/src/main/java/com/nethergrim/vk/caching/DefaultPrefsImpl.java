package com.nethergrim.vk.caching;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.enums.MainActivityState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import rx.Observable;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 3/20/15.
 */
public class DefaultPrefsImpl implements Prefs {

    private static final String KEY_USER_ID = "id";
    private static final String KEY_ACTIVITY_STATE_ID = "activity_state_id";
    private static final String KEY_GCM_TOKEN = "gcm_token";
    private static final String KEY_UNREAD_MESSAGES_COUNT = "unread_messages_count";
    private static final String KEY_FRIENDS_COUNT = "fr_c";
    private static final String KEY_KEYBOARD_HEIGHT = "keyboard_height";
    private static final String KEY_FETCH_STICKERS_TIMESTAMP = "fetch_stickers_timestamp";
    private static final String KEY_EMOJI_TAB = "emoji_tab";
    private static final String KEY_MARK_MESSAGES_AS_READ = "mark_as_read";
    private static final String KEY_DISPLAY_UNREAD_AS_UNREAD = "display_unread_as_unread";
    private static final String KEY_TOKEN = "token";
    private SharedPreferences mPrefs;

    public DefaultPrefsImpl() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(
                MyApplication.getInstance().getApplicationContext());
    }

    @Override
    public long getCurrentUserId() {
        return mPrefs.getLong(KEY_USER_ID, 0);
    }

    @Override
    public void setCurrentUserId(long userId) {
        mPrefs.edit().putLong(KEY_USER_ID, userId).apply();
    }

    @Override
    public String getToken() {
        return mPrefs.getString(KEY_TOKEN, null);
    }

    @Override
    public void setToken(String token) {
        mPrefs.edit().putString(KEY_TOKEN, token).apply();
    }

    @Override
    public int getCurrentActivityStateId() {
        return mPrefs.getInt(KEY_ACTIVITY_STATE_ID, MainActivityState.Conversations.getId());
    }

    @Override
    public void setCurrentActivityStateId(int id) {
        mPrefs.edit().putInt(KEY_ACTIVITY_STATE_ID, id).apply();
    }

    @Override
    public String getGcmToken() {
        return mPrefs.getString(KEY_GCM_TOKEN, null);
    }

    @Override
    public void setGcmToken(String token) {
        mPrefs.edit().putString(KEY_GCM_TOKEN, token).apply();
    }

    @Override
    public int getUnreadMessagesCount() {
        return mPrefs.getInt(KEY_UNREAD_MESSAGES_COUNT, 0);
    }

    @Override
    public void setUnreadMessagesCount(int count) {
        mPrefs.edit().putInt(KEY_UNREAD_MESSAGES_COUNT, count).apply();
    }

    @Override
    public int getFriendsCount() {
        return mPrefs.getInt(KEY_FRIENDS_COUNT, 0);
    }

    @Override
    public void setFriendsCount(int count) {
        mPrefs.edit().putInt(KEY_FRIENDS_COUNT, count).apply();
    }

    @Override
    public int getKeyboardHeight() {
        return mPrefs.getInt(KEY_KEYBOARD_HEIGHT + "_" + getOrientation(), 0);
    }

    @Override
    public void setKeyboardHeight(int heightPx) {
        mPrefs.edit().putInt(KEY_KEYBOARD_HEIGHT + "_" + getOrientation(), heightPx).apply();
    }

    @Override
    public int getOrientation() {
        return MyApplication.getInstance().getResources().getConfiguration().orientation;
    }

    @Override
    public long getLastFetchStickersTime() {
        return mPrefs.getLong(KEY_FETCH_STICKERS_TIMESTAMP, 0);
    }

    @Override
    public void setLastFetchStickersTime(long timestamp) {
        mPrefs.edit().putLong(KEY_FETCH_STICKERS_TIMESTAMP, timestamp).apply();
    }

    @Override
    public int getCurrentEmojiTab() {
        return mPrefs.getInt(KEY_EMOJI_TAB, 0);
    }

    @Override
    public void setCurrentEmojiTab(int tabNum) {
        mPrefs.edit().putInt(KEY_EMOJI_TAB, tabNum).apply();
    }

    @Override
    public void setMarkMessagesAsRead(boolean markMessagesAsRead) {
        mPrefs.edit().putBoolean(KEY_MARK_MESSAGES_AS_READ, markMessagesAsRead).apply();
    }

    @Override
    public boolean markMessagesAsRead() {
        return mPrefs.getBoolean(KEY_MARK_MESSAGES_AS_READ, true);
    }

    @Override
    public void setDisplayUnreadMessagesAsUnread(boolean accessible) {
        mPrefs.edit().putBoolean(KEY_DISPLAY_UNREAD_AS_UNREAD, accessible).apply();
    }

    @Override
    public boolean isDisplayingUnreadMessagesAsUnread() {
        return mPrefs.getBoolean(KEY_DISPLAY_UNREAD_AS_UNREAD, true);
    }

    @Override
    @WorkerThread
    public synchronized void addConversationToSyncUnreadMessages(long conversationId, long toTime) {
        Set<MarkConversationReadTask> data = getConversationsToSyncUnreadMessages();
        data.add(new MarkConversationReadTask(conversationId, toTime));
        setLongToLongSet(data);
    }

    @Override
    @WorkerThread
    public synchronized void removeConversationToSyncUnreadMessages() {
        setLongToLongSet(null);
    }

    @Override
    @WorkerThread
    public synchronized Set<MarkConversationReadTask> getConversationsToSyncUnreadMessages() {
        return getUnsyncedConversationsReadTask()
                .toList()
                .map(HashSet::new)
                .toBlocking()
                .first();

    }


    private Observable<MarkConversationReadTask> getUnsyncedConversationsReadTask() {
        Realm realm = Realm.getDefaultInstance();
        List<MarkConversationReadTask> result = realm.copyFromRealm(realm.where(MarkConversationReadTask.class).findAll());
        realm.close();
        return Observable.from(result);
    }

    private void setLongToLongSet(@Nullable Set<MarkConversationReadTask> data) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(realm1 -> {
            realm1.delete(MarkConversationReadTask.class);
            if (data != null) {
                realm1.copyToRealmOrUpdate(data);
            }
        });
    }

}
