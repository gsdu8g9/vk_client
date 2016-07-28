package com.nethergrim.vk.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserUtils;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 4/7/15.
 */
public class ImageLoaderImpl implements ImageLoader {

    private Context context;
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

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
    public void loadImage(@NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context).load(url).dontAnimate().into(imageView);
    }

    @Override
    public void loadCircleImage(@NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context).load(url)
                .bitmapTransform(new CircleTransform(context))
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public void preCache(@NonNull String url) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            mainThreadHandler.post(() -> preCache(url));
            return;
        }
        Glide.with(context).load(url).preload();
    }

    @Override
    @Nullable
    public Bitmap getBitmapImmediately(@NonNull String url) {
        return getBitmapObservable(url).first().toBlocking().first();
    }


    private Observable<Bitmap> getBitmapObservable(String src) {
        preCache(src);
        return Observable.fromCallable(() -> Glide.with(context).load(src).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()).subscribeOn(Schedulers.io());
    }

}
