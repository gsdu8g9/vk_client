package com.nethergrim.vk.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.devspark.robototextview.widget.RobotoEditText;
import com.nethergrim.vk.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author andrej on 06.08.15.
 */
public class InputMessagesController extends FrameLayout implements TextWatcher {

    @InjectView(R.id.btn_emoji)
    ImageButton mBtnEmoji;
    @InjectView(R.id.et_message)
    RobotoEditText mEtMessage;
    @InjectView(R.id.btn_send)
    ImageButton mBtnSend;
    private InputMessagesControllerInterface mCallback;

    public interface InputMessagesControllerInterface {

        void onMessageSent(String text);

        void onEmojiPressed();
    }

    public InputMessagesController(Context context) {
        super(context);
        init(context);
    }

    public InputMessagesController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InputMessagesController(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setSendButtonVisibility(s != null && s.length() > 0);
    }

    public InputMessagesControllerInterface getCallback() {
        return mCallback;
    }

    public void setCallback(InputMessagesControllerInterface callback) {
        mCallback = callback;
    }

    public ImageButton getBtnSend() {
        return mBtnSend;
    }

    public void setBtnSend(ImageButton btnSend) {
        mBtnSend = btnSend;
    }

    public RobotoEditText getEtMessage() {
        return mEtMessage;
    }

    public void setEtMessage(RobotoEditText etMessage) {
        mEtMessage = etMessage;
    }

    public ImageButton getBtnEmoji() {
        return mBtnEmoji;
    }

    public void setBtnEmoji(ImageButton btnEmoji) {
        mBtnEmoji = btnEmoji;
    }

    @OnClick(R.id.btn_emoji)
    public void onEmojiClicked() {
        if (mCallback != null) {
            mCallback.onEmojiPressed();
        }
    }

    @OnClick(R.id.btn_send)
    public void onSendClicked() {
        if (mCallback != null) {
            mCallback.onMessageSent(mEtMessage.getText().toString());
        }
        mEtMessage.setText(null);
    }

    private void setSendButtonVisibility(boolean on) {
        mBtnSend.setVisibility(on ? VISIBLE : GONE);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        LayoutInflater.from(context)
                .inflate(R.layout.input_message_controller, this, true);
        ButterKnife.inject(this, this);
        mEtMessage.addTextChangedListener(this);
    }

}
