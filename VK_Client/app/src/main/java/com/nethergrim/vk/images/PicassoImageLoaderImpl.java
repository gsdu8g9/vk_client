package com.nethergrim.vk.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public class PicassoImageLoaderImpl implements ImageLoader {

    private Context context;

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
