package com.nethergrim.vk.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 28.07.15.
 */
public class VarColumnGridLayoutManager extends GridLayoutManager {

    private int minItemWidth;

    public VarColumnGridLayoutManager(Context context, int minItemWidth) {
        super(context, 1);
        this.minItemWidth = minItemWidth;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler,
            RecyclerView.State state) {
        updateSpanCount();
        super.onLayoutChildren(recycler, state);
    }

    private void updateSpanCount() {
        int spanCount = getWidth() / minItemWidth;
        if (spanCount < 1) {
            spanCount = 1;
        }
        this.setSpanCount(spanCount);
    }

}


