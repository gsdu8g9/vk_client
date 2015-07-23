package com.nethergrim.vk.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.enums.MainActivityState;
import com.nethergrim.vk.fragment.MessagesFragment;
import com.nethergrim.vk.gcm.PushNotificationsRegisterService;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.utils.Utils;
import com.nethergrim.vk.web.WebRequestManager;
import com.nethergrim.vk.web.images.ImageLoader;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;

public class MainActivity extends AbstractActivity implements WebCallback<User>,
        View.OnClickListener {

    @InjectView(R.id.messagesImageButton)
    ImageButton mMessagesImageButton;
    @InjectView(R.id.friendsImageButton)
    ImageButton mFriendsImageButton;
    @InjectView(R.id.profileImageButton)
    ImageButton mProfileImageButton;
    @InjectView(R.id.settingsImageButton)
    ImageButton mSettingsImageButton;
    @InjectView(R.id.photosImageButton)
    ImageButton mPhotosImageButton;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.imageViewSearch)
    ImageView mSearchImageView;

    @Inject
    WebRequestManager mWebRequestManager;
    @Inject
    ImageLoader mIL;
    @Inject
    Prefs mPrefs;
    @Inject
    UserProvider mUP;

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
        Log.e("TAG", "error on get current user: " + e.errorMessage);
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
            case R.id.imageViewSearch:
                // TODO open search activity
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        MyApplication.getInstance().getMainComponent().inject(this);
        PushNotificationsRegisterService.start(this);
        initMenu();
        initToolbar();
        loadCurrentUser();
    }

    private void loadCurrentUser() {
        mWebRequestManager.getCurrentUser(this);
        if (mPrefs.getCurrentUserId() != 0) {

            User user = mUP.getUser(mPrefs.getCurrentUserId());
            if (user != null) {
                onResponseSucceed(user);
            }
        }
    }

    private void initToolbar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mSearchImageView.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_search, R.color.icons));
        mSearchImageView.setOnClickListener(this);
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
                    // TODO show friends fragment
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
