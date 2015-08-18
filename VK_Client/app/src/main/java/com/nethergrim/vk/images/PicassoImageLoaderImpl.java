package com.nethergrim.vk.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nethergrim.vk.R;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.UserUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

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
        Picasso.with(context).cancelRequest(imageView);
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
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.ic_action_account_circle)
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
    public Bitmap getBitmap(@NonNull String url) {
        try {
            return Picasso.with(context).load(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bitmap getBitmap(@NonNull User user) {
        return getBitmap(UserUtils.getStablePhotoUrl(user));
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



            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setAntiAlias(true);
            canvas.drawCircle((source.getWidth()) / 2, (source.getHeight()) / 2, radius - 2,
                    paint1);
            if (source != output) {
                source.recycle();
            }
            return output;
        }

        @Override
        public String key() {
            return "rounded";
        }
    }
}
