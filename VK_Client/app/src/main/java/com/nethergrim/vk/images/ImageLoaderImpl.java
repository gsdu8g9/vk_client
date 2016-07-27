package com.nethergrim.vk.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;

import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public class ImageLoaderImpl implements ImageLoader {

    private static final String TAG = "ImageLoaderImpl";

    private Context context;
    private LruCache<String, Bitmap> mBitmapLruCache;


    public ImageLoaderImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public Observable<Bitmap> getUserAvatar(@NonNull User user) {
        final String url = UserUtils.getStablePhotoUrl(user);
        return getBitmapObservable(url);
    }

    @NonNull
    @Override
    public Observable<Bitmap> getBitmap(@NonNull String url) {
        return getBitmapObservable(url);
    }

    @NonNull
    @Override
    public Observable<Bitmap> getBitmap(@NonNull User user) {
        return getBitmap(UserUtils.getStablePhotoUrl(user));
    }

    @Override
    public void preCache(@NonNull String url) {
        getBitmapObservable(url)
                .observeOn(Schedulers.io())
                .subscribe(bitmap -> {
                    Log.d(TAG, "preCache: done for " + url);
                }, throwable -> {
                    Log.e(TAG, "preCache: ", throwable);
                });
    }

    @Override
    @Nullable
    public Bitmap getBitmapImmediately(@NonNull String url) {
        return getBitmapObservable(url).first().toBlocking().first();
    }

    private synchronized void addToCache(String url, Bitmap bitmap) {
        if (mBitmapLruCache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 4;
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

    private Observable<Bitmap> getBitmapObservable(String src) {
        Observable<Bitmap> bitmapObservable = Observable.create(
                new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(final Subscriber<? super Bitmap> subscriber) {
                        if (subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                            return;
                        }

                        if (mBitmapLruCache != null) {
                            Bitmap result = mBitmapLruCache.get(src);
                            if (result != null) {
                                subscriber.onNext(result);
                                subscriber.onCompleted();
                            }
                        }

                        try {
                            URL url = new URL(src);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap myBitmap = BitmapFactory.decodeStream(input);
                            addToCache(src, myBitmap);
                            subscriber.onNext(myBitmap);
                            subscriber.onCompleted();
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }
                });
        bitmapObservable.subscribeOn(Schedulers.io());
        return bitmapObservable;
    }

}
