package com.nethergrim.vk.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * @author andrej on 06.08.15.
 */
public class InputMessagesController extends RecyclerView {

    private EditText mEditText;
    private ImageButton mBtnSend;
    private ImageButton mBtnEmoji;

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
    }
}
