package com.nethergrim.vk.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;

import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public class PicassoImageLoaderImpl implements ImageLoader {

    private Context context;
    private LruCache<String, Bitmap> mBitmapLruCache;


    public PicassoImageLoaderImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<Bitmap> getUserAvatar(@NonNull User user) {
        final String url = UserUtils.getStablePhotoUrl(user);
        return getBitmapObservable(url);
    }

    @Override
    public Observable<Bitmap> getBitmap(@NonNull String url) {
        return getBitmapObservable(url);
    }

    @Override
    public Observable<Bitmap> getBitmap(@NonNull User user) {
        return getBitmap(UserUtils.getStablePhotoUrl(user));
    }

    @Override
    public void cacheToMemory(String url) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Picasso.with(context).load(url).config(Bitmap.Config.RGB_565).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    addToCache(url, bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        });

    }

    @Override
    @Nullable
    public Bitmap getBitmapSync(String url) {
        if (mBitmapLruCache != null) {
            Bitmap result = mBitmapLruCache.get(url);
            if (result != null) {
                return result;
            }
        }
        try {
            return Picasso.with(context).load(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addToCache(String url, Bitmap bitmap) {
        if (mBitmapLruCache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            mBitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
        if (mBitmapLruCache.get(url) == null) {
            mBitmapLruCache.put(url, bitmap);
        }
    }

    private Observable<Bitmap> getBitmapObservable(String url) {
        Observable<Bitmap> bitmapObservable = Observable.create(
                new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(final Subscriber<? super Bitmap> subscriber) {
                        if (subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                            return;
                        }
                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            Log.i("ISSUE5", " call - main thread ");
                        }
                        Target target = new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Log.i("picasso", " onBitmapLoaded ");
                                if (Looper.myLooper() == Looper.getMainLooper()) {
                                    Log.i("ISSUE5", " Target - onBitmapLoaded - main thread ");
                                }
                                subscriber.onNext(bitmap);
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                subscriber.onError(new Exception(" Failed to load bitmap"));
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        };
                        Picasso.with(context).load(url)
                                .config(Bitmap.Config.RGB_565)
                                .into(target);
                    }
                });
        bitmapObservable.subscribeOn(AndroidSchedulers.mainThread());
        bitmapObservable.observeOn(Schedulers.io());
        return bitmapObservable;
    }

}
