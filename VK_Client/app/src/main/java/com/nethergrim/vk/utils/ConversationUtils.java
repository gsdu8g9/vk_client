package com.nethergrim.vk.utils;

import android.support.annotation.NonNull;

import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.Message;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class ConversationUtils {

    public static boolean isConversationAGroupChat(@NonNull Conversation conversation) {
        return conversation.getMessage().getChat_id() > 0;
    }

    public static boolean isConversationUnread(@NonNull Conversation conversation) {
        return conversation.getUnread() > 0;
    }

    public static boolean isMessageFromMe(@NonNull Message message) {
        return message.getOut() == 1;
    }
}
