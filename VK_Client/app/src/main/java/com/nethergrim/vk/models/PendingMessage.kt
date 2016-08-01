@file:Suppress("NOTHING_TO_INLINE")

package com.nethergrim.vk.models

import android.os.Parcel
import android.os.Parcelable
import com.nethergrim.vk.utils.ConversationUtils
import java.util.*


data class PendingMessage(val randomId: Long, val text: String?, val forwardedMessageIds: String?, val stickerId: Long?, val peerId: Long) : Parcelable {

    constructor(source: Parcel) : this(source.readLong(), source.readString(), source.readString(), source.readLong(), source.readLong())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeLong(randomId)
        dest?.writeString(text)
        dest?.writeString(forwardedMessageIds)
        dest?.writeLong(stickerId ?: 0L)
        dest?.writeLong(peerId)
    }

    fun mapToMessage(currentUserId: Long): Message {
        val message = Message()
        message.id = this.randomId
        message.read_state = 0
        message.out = 1
        message.isPending = true
        message.date = System.currentTimeMillis() / 1000
        message.from_id = currentUserId
        message.body = this.text
        if (ConversationUtils.isPeerIdAGroupChat(this.peerId)) {
            message.chat_id = ConversationUtils.getConversationIdFromPeerId(this.peerId)
        } else {
            message.user_id = ConversationUtils.getUserIdFromPeerId(this.peerId)
        }
        // TODO #134 add support for stickers and forwarded messages
        return message
    }

    companion object {

        val rand = Random()

        @Suppress("unused")
        @JvmField val CREATOR: Parcelable.Creator<PendingMessage> = object : Parcelable.Creator<PendingMessage> {
            override fun createFromParcel(source: Parcel): PendingMessage = PendingMessage(source)
            override fun newArray(size: Int): Array<PendingMessage?> = arrayOfNulls(size)
        }

        inline fun generateRandomId(): Long {
            return rand.nextLong()
        }

        inline fun fromMessage(message: Message): PendingMessage {
            // TODO #134 add support for stickers and forwarded messages
            val pendingMessage = PendingMessage(message.id, message.body, null, null, message.peerId)

            return pendingMessage
        }

        inline fun fromMessages(messages: List<Message>): List<PendingMessage> {
            return messages.mapNotNull { fromMessage(it) }
        }
    }
}