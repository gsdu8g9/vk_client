package com.nethergrim.vk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.devspark.robototextview.widget.RobotoEditText;
import com.nethergrim.vk.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andrej on 06.08.15.
 */
public class InputMessagesController extends FrameLayout {

    @InjectView(R.id.btn_emoji)
    ImageButton mBtnEmoji;
    @InjectView(R.id.et_message)
    RobotoEditText mEtMessage;
    @InjectView(R.id.btn_send)
    ImageButton mBtnSend;

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

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        LayoutInflater.from(context)
                .inflate(R.layout.input_message_controller, this, true);
        ButterKnife.inject(this, this);
    }
}
