package com.nethergrim.vk.utils;

import android.text.TextUtils;

import com.kisstools.utils.StringUtil;
import com.nethergrim.vk.models.User;

import java.util.Arrays;
import java.util.List;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class UserUtils {

    public static List<String> getDefaultUserFields() {
        return Arrays.asList(User.Fields.photo_200, User.Fields.online, User.Fields.sex,
                User.Fields.photo_max, User.Fields.photo_100, User.Fields.photo_200_orig,
                User.Fields.photo_max_orig);
    }

    public static String getDefaultUserFieldsAsString() {
        List<String> fields = UserUtils.getDefaultUserFields();

        StringBuilder sb = new StringBuilder();
        for (String field : fields) {
            sb.append(field);
            sb.append(", ");
        }

        return StringUtil.cutText(sb.toString(), sb.toString().length() - 2);
    }

    public static String getStablePhotoUrl(User user) {
        String url = user.getPhoto_200();
        if (TextUtils.isEmpty(url)) {
            url = user.getPhoto_200_orig();
        }
        if (TextUtils.isEmpty(url)) {
            url = user.getPhoto_max_orig();
        }
        return url;
    }
}
