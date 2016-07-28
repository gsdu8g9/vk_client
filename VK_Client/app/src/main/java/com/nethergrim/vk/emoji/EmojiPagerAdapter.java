package com.nethergrim.vk.emoji;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.models.StickerDbItem;

import java.util.List;

import javax.inject.Inject;

import github.ankushsachdeva.emojicon.EmojiconGridView;
import io.realm.Realm;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 15.10.15.
 */
public class EmojiPagerAdapter extends PagerAdapter {

    @Inject
    ImageLoader mImageLoader;
    private EmojiconGridView.OnEmojiconClickedListener mCallback;
    private List<StickerDbItem> stickerDbItems;

    public EmojiPagerAdapter(EmojiconGridView.OnEmojiconClickedListener callback) {
        MyApplication.getInstance().getMainComponent().inject(this);
        this.mCallback = callback;
        Realm realm = Realm.getDefaultInstance();

        stickerDbItems = realm.where(StickerDbItem.class).findAll();
        realm.close();
    }

    public List<StickerDbItem> getStickerDbItems() {
        return stickerDbItems;
    }

    @Override
    public int getCount() {
        return stickerDbItems.size() + 1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v;
        if (position == 0) {
            EmojiconGridView emojiconGridView = new EmojiconGridView(container.getContext(),
                    mCallback);
            v = emojiconGridView.getRootView();
        } else {
            v = new StickersLayoutView(container.getContext(), stickerDbItems.get(position - 1),
                    mCallback);
        }
        container.addView(v);
        return v;
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
