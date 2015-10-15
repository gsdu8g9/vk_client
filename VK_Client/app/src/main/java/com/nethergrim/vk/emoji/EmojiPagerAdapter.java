package com.nethergrim.vk.emoji;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nethergrim.vk.models.StickerDbItem;

import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.emoji.People;
import io.realm.Realm;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 15.10.15.
 */
public class EmojiPagerAdapter extends PagerAdapter {

    private List<StickerDbItem> mStickerDbItems;
    private EmojiconGridView.OnEmojiconClickedListener mCallback;

    public EmojiPagerAdapter(EmojiconGridView.OnEmojiconClickedListener callback) {
        this.mCallback = callback;
        Realm realm = Realm.getDefaultInstance();
        mStickerDbItems = realm.where(StickerDbItem.class).findAll();
        realm.close();
    }

    @Override
    public int getCount() {
        return mStickerDbItems.size() + 1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Context ctx = container.getContext();
        if (position == 0) {
            // regular emojis
            return new EmojiconGridView(ctx, People.DATA, null, mCallback);
        } else {
            // stickers =)
        }
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
