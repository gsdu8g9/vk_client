package com.nethergrim.vk.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com).
 *         All rights reserved!
 */
public class PaginationManager extends RecyclerView.OnScrollListener {

    private final int mDefaultPageSize;
    private final OnRecyclerViewScrolledToPageListener mCallback;
    private int mOffset;
    private LinearLayoutManager mLayoutManager;
    private boolean loading;
    private int previousTotal;

    /**
     * For stacking list from the bottom. Will call {@link com.nethergrim.vk.views
     * .PaginationManager.OnRecyclerViewScrolledToPageListener#onRecyclerViewScrolledToPage(int)
     * after scrolling to the top}
     */
    private boolean mStackFromBottom;

    public interface OnRecyclerViewScrolledToPageListener {

        void onRecyclerViewScrolledToPage(int pageNumber);
    }

    public PaginationManager(int defaultPageSize,
            OnRecyclerViewScrolledToPageListener callback,
            int offset) {
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

    public PaginationManager(int defaultPageSize,
            OnRecyclerViewScrolledToPageListener callback,
            int offset, boolean stackFromBottom) {
        this.mCallback = callback;
        if (mCallback == null) {
            throw new IllegalStateException("OnRecyclerViewScrolledToPageListener is NULL");
        }
        this.mStackFromBottom = stackFromBottom;
        this.mDefaultPageSize = defaultPageSize;
        this.mOffset = offset;

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
        if (mLayoutManager == null) {
            mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        }

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = firstVisibleItem + visibleItemCount;

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (mStackFromBottom) {
            if (!loading && firstVisibleItem <= mOffset) {
                mCallback.onRecyclerViewScrolledToPage(totalItemCount / mDefaultPageSize);
            }
        } else {

            if (!loading && (lastVisibleItem + mOffset) >= totalItemCount) {
                // End has been reached

                mCallback.onRecyclerViewScrolledToPage(totalItemCount / mDefaultPageSize + 1);

                // Do something

                loading = true;
            }
        }

    }
}
