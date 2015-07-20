package com.nethergrim.vk.web.images;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nethergrim.vk.R;
import com.nethergrim.vk.models.User;

/**
 * @author andrej on 11.07.15.
 */
public class GlideImageLoaderImpl implements ImageLoader {

    private Context mCtx;

    public GlideImageLoaderImpl(Context context) {
        this.mCtx = context;
    }

    @Override
    public void displayUserAvatar(@NonNull User user, @NonNull ImageView imageView) {
        boolean deactivated = user.getDeactivated() != null && user.getDeactivated().length() > 0;
        if (deactivated) {
            Glide.with(mCtx).load(R.drawable.ic_deactivated_200).centerCrop().into(imageView);
        } else {
            String avatarUrl = user.getPhoto_200();
            Glide.with(mCtx)
                    .load(avatarUrl)
                    .fallback(R.drawable.ic_action_account_circle)
                    .centerCrop()
                    .placeholder(R.drawable.ic_action_account_circle)
                    .into(imageView);
        }
    }

    @Override
    public void displayImage(@NonNull String url, @NonNull ImageView imageView) {
        Glide.with(mCtx).load(url).into(imageView);
    }
}
