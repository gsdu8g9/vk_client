package com.nethergrim.vk.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author andrej on 29.07.15.
 */
public class UserRelationsView extends RecyclerView {

    public UserRelationsView(Context context) {
        super(context);
        init(context);
    }

    public UserRelationsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UserRelationsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }

    }
}
