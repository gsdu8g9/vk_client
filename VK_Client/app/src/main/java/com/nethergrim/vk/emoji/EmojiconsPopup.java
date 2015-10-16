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
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.caching.Prefs;
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

public class EmojiconsPopup extends PopupWindow implements ViewPager.OnPageChangeListener {

    public static final String TAG = EmojiconsPopup.class.getSimpleName();
    OnEmojiconClickedListener onEmojiconClickedListener;
    @Inject
    ImageLoader mImageLoader;

    @Inject
    Prefs mPrefs;
    private View rootView;
    private Context mContext;


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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPrefs.setCurrentEmojiTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private View createCustomView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(github.ankushsachdeva.emojicon.R.layout.emojicons, null,
                false);

        // pager init
        ViewPager emojisPager = (ViewPager) view.findViewById(
                github.ankushsachdeva.emojicon.R.id.emojis_pager);
        EmojiPagerAdapter adapter = new EmojiPagerAdapter(onEmojiconClickedListener);
        emojisPager.setAdapter(adapter);
        emojisPager.addOnPageChangeListener(this);

        // tabs init
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(emojisPager);
        Resources res = mContext.getResources();
        List<StickerDbItem> stickers = adapter.getStickerDbItems();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i == 0) {
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_emoji_people_light_activated);
            } else {
                final int finalI = i - 1;
                Observable.just(stickers.get(finalI).getPhoto())
                        .flatMap(mImageLoader::getBitmap)
                        .map(bitmap -> new BitmapDrawable(res, bitmap))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(drawable -> {
                            tabLayout.getTabAt(finalI + 1).setIcon(drawable);
                        });
            }
        }

        emojisPager.setCurrentItem(mPrefs.getCurrentEmojiTab());
        return view;
    }

}
