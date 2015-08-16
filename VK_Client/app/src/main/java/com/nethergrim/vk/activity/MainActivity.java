package com.nethergrim.vk.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.callbacks.ToolbarScrollable;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.enums.MainActivityState;
import com.nethergrim.vk.event.ConversationsUpdatedEvent;
import com.nethergrim.vk.event.MyUserUpdatedEvent;
import com.nethergrim.vk.fragment.FriendsFragment;
import com.nethergrim.vk.fragment.MessagesFragment;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.utils.Utils;
import com.nethergrim.vk.views.MenuButton;
import com.nethergrim.vk.web.DataManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;

public class MainActivity extends AbstractActivity implements WebCallback<User>,
        View.OnClickListener, ToolbarScrollable {


    public static final String TAG = MainActivity.class.getName();
    @Inject
    DataManager mDataManager;
    @Inject
    Bus mBus;
    @Inject
    ImageLoader mIL;
    @Inject
    Prefs mPrefs;
    @Inject
    UserProvider mUP;
    @InjectView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.messagesImageButton)
    MenuButton mMessagesImageButton;
    @InjectView(R.id.friendsImageButton)
    MenuButton mFriendsImageButton;
    @InjectView(R.id.profileImageButton)
    ImageButton mProfileImageButton;
    @InjectView(R.id.photosImageButton)
    MenuButton mPhotosImageButton;
    @InjectView(R.id.settingsImageButton)
    MenuButton mSettingsImageButton;
    @InjectView(R.id.menu_layout)
    LinearLayout mMenuLayout;
    @InjectView(R.id.toolbar_layout)
    View mToolbarLayout;
    private boolean mToolbarIsHidden = false;
    private float mToolBarHeight;

    private MainActivityState mCurrentState;

    @Override
    public void onResponseSucceed(final User response) {
        mIL.displayUserAvatar(response, mProfileImageButton);
        mPrefs.setCurrentUserId(response.getId());
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(response);
            }
        });
    }

    @Override
    public void onResponseFailed(VKError e) {
        Log.e(TAG, "error on get current user: " + e.toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.messagesImageButton:
                setState(MainActivityState.Conversations);
                break;
            case R.id.friendsImageButton:
                setState(MainActivityState.Friends);
                break;
            case R.id.profileImageButton:
                setState(MainActivityState.Profile);
                break;
            case R.id.settingsImageButton:
                setState(MainActivityState.Settings);
                break;
            case R.id.photosImageButton:
                setState(MainActivityState.Photos);
                break;
        }
    }

    @Subscribe
    public void loadCurrentUser(MyUserUpdatedEvent r) {
        if (mPrefs.getCurrentUserId() != 0) {
            User user = mUP.getUser(mPrefs.getCurrentUserId());
            if (user != null) {
                onResponseSucceed(user);
            }
        }
    }

    @Subscribe
    public void onConversationsUpdated(ConversationsUpdatedEvent e) {
        mMessagesImageButton.setAlert(mPrefs.getUnreadMessagesCount() > 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_menu_item) {
            // TODO open search activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToolbar() {
        if (mToolbarIsHidden) {
            mToolbarIsHidden = false;
            mToolbarLayout.animate()
                    .translationY(0)
                    .setDuration(Constants.ANIMATION_DURATION)
                    .start();
        }
    }

    @Override
    public void hideToolbar() {
        if (!mToolbarIsHidden) {
            mToolbarIsHidden = true;
            mToolbarLayout.animate()
                    .translationY(mToolBarHeight * -1)
                    .setDuration(Constants.ANIMATION_DURATION)
                    .start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        MyApplication.getInstance().getMainComponent().inject(this);
        mBus.register(this);
        initMenu();
        initToolbar();
        loadCurrentUser(new MyUserUpdatedEvent());
        onConversationsUpdated(new ConversationsUpdatedEvent());
        mDataManager.launchStartupTasks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    private void initToolbar() {
        mToolBarHeight = getResources().getDimensionPixelSize(R.dimen.action_bar_height);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
    }

    private void initMenu() {
        mMessagesImageButton.setOnClickListener(this);
        mFriendsImageButton.setOnClickListener(this);
        mProfileImageButton.setOnClickListener(this);
        mPhotosImageButton.setOnClickListener(this);
        mSettingsImageButton.setOnClickListener(this);
        setState(MainActivityState.getStateForId(mPrefs.getCurrentActivityStateId()));
        mProfileImageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    private void setState(@NonNull MainActivityState mainActivityState) {
        if (!mainActivityState.equals(mCurrentState)) {
            mCurrentState = mainActivityState;
            mPrefs.setCurrentActivityStateId(mainActivityState.getId());
            deselectIcons();
            showToolbar();
            mToolbar.setTitle(mainActivityState.getTitleStringRes());
            switch (mainActivityState) {
                case Conversations:
                    mMessagesImageButton.setImageDrawable(
                            Utils.tintIcon(R.drawable.ic_action_question_answer, R.color.primary));
                    showFragment(new MessagesFragment(), false, false, R.id.fragment_container);
                    break;
                case Friends:
                    mFriendsImageButton.setImageDrawable(
                            Utils.tintIcon(R.drawable.ic_action_account_child, R.color.primary));
                    showFragment(new FriendsFragment(), false, false, R.id.fragment_container);
                    break;
                case Profile:
                    // TODO show profile fragment
                    break;
                case Photos:
                    mPhotosImageButton.setImageDrawable(
                            Utils.tintIcon(R.drawable.ic_image_collections, R.color.primary));
                    // TODO show Photos fragment
                    break;
                case Settings:
                    mSettingsImageButton.setImageDrawable(
                            Utils.tintIcon(R.drawable.ic_action_settings, R.color.primary));
                    // TODO show settings fragment
                    break;
            }
        }
    }

    private void deselectIcons() {
        mMessagesImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_question_answer, R.color.icons_color));
        mFriendsImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_account_child, R.color.icons_color));
        mSettingsImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_settings, R.color.icons_color));
        mPhotosImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_image_collections, R.color.icons_color));
    }
}
