package com.nethergrim.vk.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * @author andreydrobyazko on 4/7/15.
 */
public class ImageLoaderImpl implements ImageLoader {

    private Context context;

    public ImageLoaderImpl(Context context) {
        this.context = context;
    }

    @Override
    public void displayAvatar(String url, ImageView imageView) {
        Picasso.with(context).load(url).fit().centerCrop().noFade().config(Bitmap.Config.RGB_565).transform(new RoundedTransformation()).into(imageView);
    }

    public class RoundedTransformation implements com.squareup.picasso.Transformation {


        @Override
        public Bitmap transform(final Bitmap source) {
            int radius = Math.max(source.getWidth(), source.getHeight()) / 2;

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawCircle((source.getWidth()) / 2, (source.getHeight()) / 2, radius - 2, paint);

            if (source != output) {
                source.recycle();
            }

            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setAntiAlias(true);
            canvas.drawCircle((source.getWidth()) / 2, (source.getHeight()) / 2, radius - 2, paint1);


            return output;
        }

        @Override
        public String key() {
            return "rounded";
        }
    }
}
