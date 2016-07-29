package com.nethergrim.vk.models

import android.os.Parcel
import android.os.Parcelable
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

    companion object {

        val rand = Random()

        @Suppress("unused")
        @JvmField val CREATOR: Parcelable.Creator<PendingMessage> = object : Parcelable.Creator<PendingMessage> {
            override fun createFromParcel(source: Parcel): PendingMessage = PendingMessage(source)
            override fun newArray(size: Int): Array<PendingMessage?> = arrayOfNulls(size)
        }

        fun generateRandomId(): Long {
            return rand.nextLong()
        }
    }
}