package com.nethergrim.vk.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.devspark.robototextview.widget.RobotoEditText;
import com.devspark.robototextview.widget.RobotoTextView;
import com.nethergrim.vk.R;
import com.nethergrim.vk.activity.AbstractActivity;
import com.nethergrim.vk.adapter.SelectableUltimateAdapter;
import com.nethergrim.vk.emoji.EmojiconsPopup;
import com.nethergrim.vk.models.outcoming_attachments.BaseAttachment;
import com.nethergrim.vk.models.outcoming_attachments.MessageAttachment;
import com.nethergrim.vk.utils.RecyclerItemClickListener;
import com.nethergrim.vk.views.KeyboardDetectorRelativeLayout;

import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 03.10.15.
 */
public abstract class BaseKeyboardFragment extends AbstractFragment
        implements EmojiconGridView.OnEmojiconClickedListener,
        KeyboardDetectorRelativeLayout.IKeyboardChanged,
        RecyclerItemClickListener.OnItemClickListener {


    private static final long DELAY_MS = 30;
    protected boolean mInSelectedStateNow;
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
    @InjectView(R.id.selection_layout)
    FrameLayout mSelectionLayout;
    @InjectView(R.id.textCount)
    RobotoTextView mTextCount;
    @InjectView(R.id.buttons_layout)
    LinearLayout mButtonsLayout;
    @InjectView(R.id.btn_close)
    ImageButton mBtnClose;
    @InjectView(R.id.btn_copy)
    ImageButton mBtnCopy;
    @InjectView(R.id.btn_reply)
    ImageButton mBtnReply;
    @InjectView(R.id.btn_forward)
    ImageButton mBtnForward;
    @InjectView(R.id.btn_delete)
    ImageButton mBtnDelete;
    private boolean mShowingEmojiKeyboard = false;
    private InputMethodManager mInputMethodManager;
    private EmojiconsPopup mEmojiconsPopup;
    private SelectableUltimateAdapter mAdapter;

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
        mAdapter = getAdapter(ctx);
        mRecycler.setAdapter(mAdapter);
        initRecyclerView(mRecycler);
        preInitToolbar(mToolbar);
        initToolbar(mToolbar);
        mKeyboardDetector.addKeyboardStateChangedListener(this);
        mSelectionLayout.setVisibility(View.GONE);
        mRecycler.addOnItemTouchListener(new RecyclerItemClickListener(ctx, this));
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

            hideEmojiKeyboard();
            showSoftKeyboard();
        } else {
            // show emoji keyboard, and hide default keyboard (if it's opened)
            showSoftKeyboard();
            imageButton.setImageResource(R.drawable.ic_hardware_keyboard);
            showEmojiKeyboard();
        }
    }

    public abstract void initRecyclerView(RecyclerView recycler);

    public abstract void postText(String text);

    public abstract String getTitle();

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        if (mEditText == null || emojicon == null) {
            return;
        }

        int start = mEditText.getSelectionStart();
        int end = mEditText.getSelectionEnd();
        if (start < 0) {
            mEditText.append(emojicon.getEmoji());
        } else {
            mEditText.getText().replace(Math.min(start, end),
                    Math.max(start, end), emojicon.getEmoji(), 0,
                    emojicon.getEmoji().length());
        }
    }

    @Override
    public void onEmojiconBackPressClicked() {
        KeyEvent event = new KeyEvent(
                0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        mEditText.dispatchKeyEvent(event);
    }

    @Override
    public void onStickerClicked(long id) {
        showToast("Clicked on sticker: " + id);
    }

    @Override
    public void onKeyboardShown() {

    }

    @Override
    public void onKeyboardHidden() {
        hideEmojiKeyboard();
        mBtnLeft.setImageResource(R.drawable.ic_action_social_mood);
    }

    @Override
    public void onItemClick(View childView, int position) {
        // if is in selection state then toggle selection for item
        if (mAdapter == null) {
            return;
        }
        position = mAdapter.getDataPosition(position);
        if (mInSelectedStateNow) {
            mAdapter.toggle(position);
        }
        int selectedCount = mAdapter.getSelectedIds().size();
        if (selectedCount == 0) {
            mInSelectedStateNow = false;
            hideSelectionToolbar();
        } else {
            mTextCount.setText(String.valueOf(selectedCount));
        }
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        // move to selection state and select one item
        if (mAdapter == null) {
            return;
        }
        position = mAdapter.getDataPosition(position);

        if (!mInSelectedStateNow) {
            mInSelectedStateNow = true;
            showSelectionToolbar();
            mAdapter.toggle(position);
            mTextCount.setText("1");
        }
    }

    @OnClick(R.id.btn_close)
    public void onRemoveSelectionClicked() {
        if (mAdapter == null) {
            return;
        }
        mAdapter.clearSelection();
        hideSelectionToolbar();
        mInSelectedStateNow = false;
    }


    @OnClick(R.id.btn_copy)
    public void onCopyClicked() {
        if (mAdapter == null) {
            return;
        }
        String copiedText = getSelectedText();
        if (copiedText == null) {
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(
                Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("vk", copiedText);
        clipboard.setPrimaryClip(clip);
        showToast(R.string.text_copied_to_clipboard);
        onRemoveSelectionClicked();
    }

    @OnClick(R.id.btn_reply)
    public void onReplyClicked() {
        if (mAdapter == null) {
            return;
        }
        addAttachment(getSelectedMessages());
        onRemoveSelectionClicked();
    }

    @OnClick(R.id.btn_forward)
    public void onForwardClicked() {
        if (mAdapter == null) {
            return;
        }
        List<MessageAttachment> messages = getSelectedMessages();
        // TODO: 17.10.15 implement - go back to conversations screen, to select a person, than
        // just attach messages

        onRemoveSelectionClicked();
    }

    @OnClick(R.id.btn_delete)
    public void onDeleteClicked() {
        if (mAdapter == null) {
            return;
        }
        // TODO: 17.10.15 add asking dialog here
        Set<Long> ids = mAdapter.getSelectedIds();
        deleteSelectedMessages(ids);
        onRemoveSelectionClicked();
    }

    public abstract void deleteSelectedMessages(Set<Long> dataIds);

    public abstract List<MessageAttachment> getSelectedMessages();

    public abstract String getSelectedText();

    protected abstract SelectableUltimateAdapter getAdapter(Context context);

    protected abstract void initToolbar(Toolbar toolbar);

    private void addAttachment(List<? extends BaseAttachment> attachments) {
        // TODO: 17.10.15 implement
    }

    private void showSelectionToolbar() {
        mSelectionLayout.setVisibility(View.VISIBLE);
    }

    private void hideSelectionToolbar() {
        mSelectionLayout.setVisibility(View.GONE);
    }

    private void showSoftKeyboard() {
        if (mEditText == null) {
            return;
        }
        if (mInputMethodManager == null) {
            return;
        }
        mInputMethodManager.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideEmojiKeyboard() {
        if (mEmojiconsPopup != null) {
            mEmojiconsPopup.dismiss();
        }
        mShowingEmojiKeyboard = false;

    }

    private void showEmojiKeyboard() {
        mEmojiconsPopup = new EmojiconsPopup(mKeyboardDetector, getActivity(), this);
        mEmojiconsPopup.setKeyBoardHeight(mKeyboardDetector.getKeyboardHeight());
        mEmojiconsPopup.showAtBottom();
        mShowingEmojiKeyboard = true;
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
