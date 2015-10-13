package com.nethergrim.vk.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.devspark.robototextview.widget.RobotoEditText;
import com.nethergrim.vk.R;
import com.nethergrim.vk.activity.AbstractActivity;
import com.nethergrim.vk.views.KeyboardDetectorRelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import github.ankushsachdeva.emojicon.EmojiconsPopup;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 03.10.15.
 */
public abstract class BaseKeyboardFragment extends AbstractFragment {


    private static final long DELAY_MS = 30;
    @InjectView(R.id.btnLeft)
    ImageButton mBtnLeft;
    @InjectView(R.id.editText)
    RobotoEditText mEditText;
    @InjectView(R.id.btnRight)
    ImageButton mBtnRight;
    @InjectView(R.id.inputLayout)
    LinearLayout mInputLayout;
    @InjectView(R.id.divider)
    View mDivider;
    @InjectView(R.id.recycler)
    RecyclerView mRecycler;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.keyboardDetector)
    KeyboardDetectorRelativeLayout mKeyboardDetector;
    private boolean mShowingEmojiKeyboard = false;
    private InputMethodManager mInputMethodManager;
    private EmojiconsPopup mEmojiconsPopup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base_emoji_keyboard, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context ctx = view.getContext();
        mInputMethodManager = (InputMethodManager) ctx.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        initRecyclerView(mRecycler);
        preInitToolbar(mToolbar);
        initToolbar(mToolbar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mInputMethodManager = null;
        ButterKnife.reset(this);
    }

    public void preInitToolbar(Toolbar toolbar) {
        toolbar.setTitleTextColor(Color.WHITE);
        AbstractActivity abstractActivity = (AbstractActivity) getActivity();
        abstractActivity.setSupportActionBar(toolbar);
        abstractActivity.setTitle(getTitle());
        abstractActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.btnLeft)
    public void onLeftBtnClick(View v) {
        ImageButton imageButton = mBtnLeft;
        if (mShowingEmojiKeyboard) {
            // hide emoji keyboard and show default keyboard
            imageButton.setImageResource(R.drawable.ic_action_social_mood);
            showSoftKeyboard(mEditText);
            hideEmojiKeyboard();
        } else {
            // show emoji keyboard, and hide default keyboard (if it's opened)
            imageButton.setImageResource(R.drawable.ic_hardware_keyboard);
            showEmojiKeyboard();
        }
        mShowingEmojiKeyboard = ! mShowingEmojiKeyboard;
    }

    public abstract void initRecyclerView(RecyclerView recycler);

    public abstract void postText(String text);

    public abstract String getTitle();

    protected abstract void initToolbar(Toolbar toolbar);

    private void showSoftKeyboard(EditText editText) {
        if (editText == null) {
            return;
        }
        if (mInputMethodManager == null) {
            return;
        }
        mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideEmojiKeyboard() {
        if (mEmojiconsPopup != null) {
            mEmojiconsPopup.dismiss();
        }
    }

    private void showEmojiKeyboard() {
        mEmojiconsPopup = new EmojiconsPopup(mKeyboardDetector, getActivity());
        mEmojiconsPopup.setKeyBoardHeight(mKeyboardDetector.getKeyboardHeight());
        mEmojiconsPopup.showAtBottom();
    }

    private void hideSoftKeyboard() {
        if (mEditText == null) {
            return;
        }

        if (mInputMethodManager == null) {
            return;
        }
        mInputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
