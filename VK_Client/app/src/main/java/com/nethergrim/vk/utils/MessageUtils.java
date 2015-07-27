package com.nethergrim.vk.utils;

import android.support.annotation.NonNull;

import com.nethergrim.vk.models.Attachment;
import com.nethergrim.vk.models.Message;
import com.nethergrim.vk.models.Sticker;

import java.util.List;

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
        boolean result = false;
        if (isMessageWithAttachments(message)) {
            List<Attachment> attachmentList = message.getAttachments();
            for (int i = 0, size = attachmentList.size(); i < size; i++) {
                if (attachmentList.get(i).getPhoto() != null) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean isMessageWithAudio(@NonNull Message message) {
        boolean result = false;
        if (isMessageWithAttachments(message)) {
            List<Attachment> attachmentList = message.getAttachments();
            for (int i = 0, size = attachmentList.size(); i < size; i++) {
                if (attachmentList.get(i).getAudio() != null) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

}
