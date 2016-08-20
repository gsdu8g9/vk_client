package com.nethergrim.vk.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Random;

import io.realm.RealmList;

/**
 * Created on 21.08.16.
 */

public class PendingMessage implements Parcelable {

    public static final Creator<PendingMessage> CREATOR = new Creator<PendingMessage>() {
        @Override
        public PendingMessage createFromParcel(Parcel in) {
            return new PendingMessage(in);
        }

        @Override
        public PendingMessage[] newArray(int size) {
            return new PendingMessage[size];
        }
    };
    private static final Random rand = new Random();
    private long randomId;
    private String text;
    private String forwardedMessageIds;
    private StickerLocal sticker;
    private long peerId;

    public PendingMessage(long randomId, String text, String forwarded, long stickerId, long peerId) {
        this.randomId = randomId;
        this.text = text;
        this.forwardedMessageIds = forwarded;
        this.peerId = peerId;
        if (stickerId > 0) {
            sticker = new StickerLocal(stickerId, null);
        }
    }

    public PendingMessage() {
    }

    protected PendingMessage(Parcel in) {
        randomId = in.readLong();
        text = in.readString();
        forwardedMessageIds = in.readString();
        sticker = in.readParcelable(StickerLocal.class.getClassLoader());
        peerId = in.readLong();
    }

    public static long generateRandomId() {
        return rand.nextLong();
    }

    public static PendingMessage fromMessage(@NonNull Message message) {
        PendingMessage pendingMessageJava = new PendingMessage();
        pendingMessageJava.randomId = message.getId();
        pendingMessageJava.text = message.getBody();
        pendingMessageJava.peerId = message.getPeerId();
        pendingMessageJava.forwardedMessageIds = null; // TODO handle forwaded messages from here
        RealmList<Attachment> attachments = message.getAttachments();
        for (int i = 0, attachmentsSize = attachments.size(); i < attachmentsSize; i++) {
            Attachment attachment = attachments.get(i);
            if (attachment.getSticker() != null) {
                Sticker sticker = attachment.getSticker();
                pendingMessageJava.sticker = new StickerLocal(Long.parseLong(sticker.getId()), sticker.getPhoto256());
            }
        }
        return pendingMessageJava;
    }

    public static List<PendingMessage> fromMessages(List<Message> result) {
        return rx.Observable.from(result).map(PendingMessage::fromMessage).toList().toBlocking().first();
    }

    public long getRandomId() {
        return randomId;
    }

    public void setRandomId(long randomId) {
        this.randomId = randomId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getForwardedMessageIds() {
        return forwardedMessageIds;
    }

    public void setForwardedMessageIds(String forwardedMessageIds) {
        this.forwardedMessageIds = forwardedMessageIds;
    }

    public StickerLocal getSticker() {
        return sticker;
    }

    public void setSticker(StickerLocal sticker) {
        this.sticker = sticker;
    }

    public long getPeerId() {
        return peerId;
    }

    public void setPeerId(long peerId) {
        this.peerId = peerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(randomId);
        parcel.writeString(text);
        parcel.writeString(forwardedMessageIds);
        parcel.writeParcelable(sticker, i);
        parcel.writeLong(peerId);
    }

    public Message mapToMessage(long currentUserId) {
        // TODO implement
        return null;
    }
}
