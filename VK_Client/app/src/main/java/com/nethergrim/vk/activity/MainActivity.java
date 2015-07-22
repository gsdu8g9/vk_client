package com.nethergrim.vk.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.fragment.MessagesFragment;
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

public class MainActivity extends AbstractActivity implements WebCallback<User> {

    @InjectView(R.id.imageButton)
    ImageButton mMessagesImageButton;
    @InjectView(R.id.imageButton2)
    ImageButton mFriendsImageButton;
    @InjectView(R.id.imageButton3)
    ImageButton mProfileImageButton;
    @InjectView(R.id.imageButton4)
    ImageButton mSettingsImageButton;
    @InjectView(R.id.imageButton5)
    ImageButton mSearchImageButton;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @Inject
    WebRequestManager mWRM;
    @Inject
    ImageLoader mIL;
    @Inject
    Prefs mPrefs;
    @Inject
    UserProvider mUP;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        MyApplication.getInstance().getMainComponent().inject(this);
        showFragment(new MessagesFragment(), false, false, R.id.fragment_container);
        initMenu();
        initToolbar();
        loadCurrentUser();
    }

    private void loadCurrentUser() {
        mWRM.getCurrentUser(this);
        if (mPrefs.getCurrentUserId() != 0) {

            User user = mUP.getUser(mPrefs.getCurrentUserId());
            if (user != null) {
                onResponseSucceed(user);
            }
        }
    }

    private void initToolbar() {
        ViewCompat.setElevation(mToolbar, 4.0f * Constants.mDensity);
    }

    private void initMenu() {
        mMessagesImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_question_answer, R.color.primary));
        mFriendsImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_account_child, R.color.primary));
        mSettingsImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_settings, R.color.primary));
        mSearchImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_search, R.color.primary));

        mProfileImageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

}
