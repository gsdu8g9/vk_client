package com.nethergrim.vk.utils;

import android.support.v7.widget.RecyclerView;

import com.nethergrim.vk.callbacks.ToolbarScrollable;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 29.07.15.
 */
public class BasicRecyclerViewScroller extends RecyclerView.OnScrollListener {

    public static final int MINIMAL_OFFSET_TO_HANDLE = 10;
    private ToolbarScrollable mToolbarScrollable;

    public BasicRecyclerViewScroller(ToolbarScrollable toolbarScrollable) {
        mToolbarScrollable = toolbarScrollable;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (mToolbarScrollable != null) {
            if (dy > MINIMAL_OFFSET_TO_HANDLE) {
                mToolbarScrollable.hideToolbar();
            } else if (dy < MINIMAL_OFFSET_TO_HANDLE * -1) {
                mToolbarScrollable.showToolbar();
            }
        }
    }
}
