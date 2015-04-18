package com.nethergrim.vk.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by nethergrim on 18.04.2015.
 */
public class RecyclerviewPageScroller extends RecyclerView.OnScrollListener {

    private final int mDefaultPageSize;
    private final OnRecyclerViewScrolledToPageListener mCallback;
    private int mLastDeliveredPage;
    private int mMaxScrolledPage;
    private int mOffset;

    public RecyclerviewPageScroller(int defaultPageSize, OnRecyclerViewScrolledToPageListener callback, int offset) {
        this.mDefaultPageSize = defaultPageSize;
        this.mCallback = callback;
        this.mOffset = offset;
        if (mCallback == null) {
            throw new IllegalStateException("OnRecyclerViewScrolledToPageListener is NULL");
        }
        if (mOffset < 0) {
            mOffset *= -1;
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
//        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

        int lastVisiblePositionWithOffset = firstVisibleItemPosition + visibleItemCount + mOffset;
        if (lastVisiblePositionWithOffset % mDefaultPageSize == 0) {
            int newPageNumber = lastVisiblePositionWithOffset / mDefaultPageSize;
            if (mLastDeliveredPage != newPageNumber) {
                mLastDeliveredPage = newPageNumber;
                mCallback.onRecyclerViewScrolledToPage(newPageNumber);
            }

        }

    }

    public interface OnRecyclerViewScrolledToPageListener {
        void onRecyclerViewScrolledToPage(int pageNumber);
    }
}
