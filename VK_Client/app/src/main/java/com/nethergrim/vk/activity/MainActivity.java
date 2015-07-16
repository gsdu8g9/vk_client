package com.nethergrim.vk.activity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.nethergrim.vk.R;
import com.nethergrim.vk.fragment.MessagesFragment;
import com.nethergrim.vk.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AbstractActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        showFragment(new MessagesFragment(), false, false, R.id.fragment_container);
        initMenu();
    }

    private void initMenu() {
        mMessagesImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_question_answer, R.color.menu_button_icon));
        mFriendsImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_account_child, R.color.menu_button_icon));
        mSettingsImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_settings, R.color.menu_button_icon));
        mSearchImageButton.setImageDrawable(
                Utils.tintIcon(R.drawable.ic_action_search, R.color.menu_button_icon));
        // TODO init profile button with current profile avatar image
    }

}
