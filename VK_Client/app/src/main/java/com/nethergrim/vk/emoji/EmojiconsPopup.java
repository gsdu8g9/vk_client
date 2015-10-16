/*
 * Copyright 2014 Ankush Sachdeva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nethergrim.vk.emoji;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.models.StickerDbItem;

import java.util.List;

import javax.inject.Inject;

import github.ankushsachdeva.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author Ankush Sachdeva (sankush@yahoo.co.in).
 */

public class EmojiconsPopup extends PopupWindow {

    public static final String TAG = EmojiconsPopup.class.getSimpleName();
    OnEmojiconClickedListener onEmojiconClickedListener;
    @Inject
    ImageLoader mImageLoader;
    private OnEmojiconBackspaceClickedListener onEmojiconBackspaceClickedListener;
    private View rootView;
    private Context mContext;
    private ViewPager emojisPager;


    public interface OnEmojiconBackspaceClickedListener {

        void onEmojiconBackspaceClicked(View v);
    }


    /**
     * Constructor
     *
     * @param rootView The top most layout in your view hierarchy. The difference of this view and
     * the screen height will be used to calculate the keyboard height.
     * @param mContext The context of current activity.
     */
    public EmojiconsPopup(View rootView, Context mContext, OnEmojiconClickedListener callback) {
        super(mContext);
        MyApplication.getInstance().getMainComponent().inject(this);
        this.mContext = mContext;
        this.rootView = rootView;
        this.onEmojiconClickedListener = callback;
        setBackgroundDrawable(new ColorDrawable(0)); // no shadow for a popup view
        setContentView(createCustomView());
        setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setSize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setKeyBoardHeight(int keyBoardHeight) {
        setSize(LayoutParams.MATCH_PARENT, keyBoardHeight);
    }


    /**
     * Set the listener for the event when backspace on emojicon popup is clicked
     */
    public void setOnEmojiconBackspaceClickedListener(OnEmojiconBackspaceClickedListener listener) {
        this.onEmojiconBackspaceClickedListener = listener;
    }

    /**
     * Use this function to show the emoji popup.
     * NOTE: Since, the soft keyboard sizes are variable on different android devices, the
     * library needs you to open the soft keyboard atleast once before calling this function.
     * If that is not possible see showAtBottomPending() function.
     */
    public void showAtBottom() {
        showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }


    /**
     * Manually set the popup window size
     *
     * @param width Width of the popup
     * @param height Height of the popup
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }


    private View createCustomView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(github.ankushsachdeva.emojicon.R.layout.emojicons, null,
                false);

        // pager init
        emojisPager = (ViewPager) view.findViewById(
                github.ankushsachdeva.emojicon.R.id.emojis_pager);
        EmojiPagerAdapter adapter = new EmojiPagerAdapter(onEmojiconClickedListener);
        emojisPager.setAdapter(adapter);

        // tabs init
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(emojisPager);
        Resources res = mContext.getResources();

        List<StickerDbItem> stickers = adapter.getStickerDbItems();

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            final int finalI = i;
            final String url = stickers.get(finalI).getPhoto();
            Observable.just(finalI)
                    .flatMap(integer -> mImageLoader.getBitmap(url))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(bitmap -> {
                        Drawable d = new BitmapDrawable(res, bitmap);
                        tabLayout.getTabAt(finalI).setIcon(d);
                    }, throwable -> {
                        if (throwable instanceof IllegalStateException) {
                            IllegalStateException exception = (IllegalStateException) throwable;
                            Log.e(TAG, exception.getMessage());
                        } else {
                            Log.e(TAG, throwable.getMessage());
                        }
                    })
            ;

        }
        return view;
    }

    /**
     * A class, that can be used as a TouchListener on any view (e.g. a Button).
     * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
     * click is fired immediately, next before initialInterval, and subsequent before
     * normalInterval.
     * <p>
     * <p>Interval is scheduled before the onClick completes, so it has to run fast.
     * If it runs slow, it does not generate skipped onClicks.
     */
    public static class RepeatListener implements View.OnTouchListener {

        private final int normalInterval;
        private final View.OnClickListener clickListener;
        private Handler handler = new Handler();
        private int initialInterval;
        private View downView;
        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                if (downView == null) {
                    return;
                }
                handler.removeCallbacksAndMessages(downView);
                handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval);
                clickListener.onClick(downView);
            }
        };

        /**
         * @param initialInterval The interval before first click event
         * @param normalInterval The interval before second and subsequent click
         * events
         * @param clickListener The OnClickListener, that will be called
         * periodically
         */
        public RepeatListener(int initialInterval,
                int normalInterval,
                View.OnClickListener clickListener) {
            if (clickListener == null) {
                throw new IllegalArgumentException("null runnable");
            }

            if (initialInterval < 0 || normalInterval < 0) {
                throw new IllegalArgumentException("negative interval");
            }

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downView = view;
                    handler.removeCallbacks(handlerRunnable);
                    handler.postAtTime(handlerRunnable, downView,
                            SystemClock.uptimeMillis() + initialInterval);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.removeCallbacksAndMessages(downView);
                    downView = null;
                    return true;
            }
            return false;
        }
    }

}
