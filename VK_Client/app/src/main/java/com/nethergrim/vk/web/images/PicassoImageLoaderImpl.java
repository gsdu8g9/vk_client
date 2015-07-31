package com.nethergrim.vk.web.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nethergrim.vk.R;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.models.UserPalette;
import com.nethergrim.vk.utils.UserUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import io.realm.Realm;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public class PicassoImageLoaderImpl implements ImageLoader {

    private Context context;

    public PicassoImageLoaderImpl(Context context) {
        this.context = context;
    }

    @Override
    public void displayUserAvatar(@NonNull User user, @NonNull ImageView imageView) {
        boolean deactivated = user.getDeactivated() != null && user.getDeactivated().length() > 0;
        if (deactivated) {
            Picasso.with(context)
                    .load(R.drawable.ic_deactivated_200)
                    .fit()
                    .centerCrop()
                    .config(Bitmap.Config.RGB_565)
                    .transform(new RoundedTransformation())
                    .into(imageView);
        } else {
            String url = UserUtils.getStablePhotoUrl(user);
            if (!TextUtils.isEmpty(url)) {
                Picasso.with(context)
                        .load(url)
                        .fit()
                        .centerCrop()
                        .config(Bitmap.Config.RGB_565)
                        .placeholder(R.drawable.ic_action_account_circle)
                        .transform(new RoundedTransformation())
                        .into(imageView);
            } else {
                Picasso.with(context)
                        .load(R.drawable.ic_action_account_circle)
                        .fit()
                        .centerCrop()
                        .config(Bitmap.Config.RGB_565)
                        .transform(new RoundedTransformation())
                        .into(imageView);
            }
        }

    }

    @Override
    public void displayImage(@NonNull String url, @NonNull ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url).config(Bitmap.Config.RGB_565).into(imageView);
        }
    }

    @Override
    public void getUserAvatar(@NonNull final User user, @NonNull final Target target) {
        final String url = UserUtils.getStablePhotoUrl(user);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(url)) {
                    Picasso.with(context)
                            .load(url)
                            .config(Bitmap.Config.RGB_565)
                            .into(target);
                } else {
                    Picasso.with(context)
                            .load(R.drawable.ic_action_account_circle)
                            .config(Bitmap.Config.RGB_565)
                            .into(target);
                }
            }
        });
    }

    @Override
    public void cacheImage(@NonNull String url) {
        Picasso.with(context).load(url).fetch();
    }

    @Override
    public void cacheUserAvatars(@NonNull User user) {
        String url = UserUtils.getStablePhotoUrl(user);
        cacheImage(url);
    }

    @Override
    public void generatePaletteAndStore(@NonNull final User user) {
        Picasso.with(context)
                .load(UserUtils.getStablePhotoUrl(user))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int defaultColor = context.getResources()
                                        .getColor(R.color.primary_light);
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                UserPalette userPalette = realm.createObject(
                                        UserPalette.class);
                                userPalette.setMuted(palette.getMutedColor(defaultColor));
                                userPalette.setMutedDark(palette.getDarkMutedColor(defaultColor));
                                userPalette.setMutedLight(palette.getLightMutedColor(defaultColor));
                                userPalette.setUserId(user.getId());
                                userPalette.setVibrant(palette.getVibrantColor(defaultColor));
                                userPalette.setVibrantLight(
                                        palette.getLightVibrantColor(defaultColor));
                                userPalette.setVibrantDark(
                                        palette.getDarkVibrantColor(defaultColor));
                                realm.commitTransaction();
                            }
                        });
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
    }

    @Nullable
    @Override
    public UserPalette getUserPalette(long userId) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(UserPalette.class)
                .equalTo("userId", userId)
                .findFirst();
    }

    public class RoundedTransformation implements com.squareup.picasso.Transformation {


        @Override
        public Bitmap transform(final Bitmap source) {
            int radius = Math.max(source.getWidth(), source.getHeight()) / 2;

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawCircle((source.getWidth()) / 2, (source.getHeight()) / 2, radius - 2, paint);

            if (source != output) {
                source.recycle();
            }

            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setAntiAlias(true);
            canvas.drawCircle((source.getWidth()) / 2, (source.getHeight()) / 2, radius - 2,
                    paint1);
            return output;
        }

        @Override
        public String key() {
            return "rounded";
        }
    }
}
