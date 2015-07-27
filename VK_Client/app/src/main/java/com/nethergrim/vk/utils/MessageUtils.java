package com.nethergrim.vk.utils;

import android.support.annotation.NonNull;

import com.nethergrim.vk.models.Message;
import com.nethergrim.vk.models.Sticker;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class MessageUtils {

    public static boolean isMessageWithAttachments(@NonNull Message message) {
        return !message.getAttachments().isEmpty();
    }

    public static boolean isMessageWithSticker(@NonNull Message message) {
        return isMessageWithAttachments(message) && message.getAttachments()
                .get(0)
                .getSticker() != null;
    }

    public static Sticker getStickerFromMessage(@NonNull Message message) {
        return message.getAttachments().get(0).getSticker();
    }

    public static boolean isMessageWithPhoto(@NonNull Message message) {
        return isMessageWithAttachments(message)
                && message.getAttachments().get(0).getPhoto() != null;
    }

}
