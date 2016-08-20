package com.nethergrim.vk.models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import rx.functions.Func1;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 15.10.15.
 */
@RealmClass
public class StickersCollectionLocal implements RealmModel {

    public static final Func1<StickersCollection, StickersCollectionLocal> MAPPER
            = stockItem -> {
        StickersCollectionLocal stickerDbItem = new StickersCollectionLocal();
        stickerDbItem.setBaseUrl(stockItem.getProduct().getStickers().getBaseUrl());
        stickerDbItem.setActive(stockItem.getProduct().getActive() == 1);
        stickerDbItem.setPurchased(stockItem.getProduct().getPurchased() == 1);
        stickerDbItem.setTitle(stockItem.getProduct().getTitle());

        stickerDbItem.setId(stockItem.getProduct().getId());
        stickerDbItem.setPhoto(stockItem.getProduct().getBaseUrl() + "thumb_102.png");
        stickerDbItem.setBackground(stockItem.getBackground());
        List<Long> ids = stockItem.getProduct().getStickers().getStickerIds();
        RealmList<StickerLocal> stickerIds = new RealmList<>();


        for (int i = 0, size = ids.size(); i < size; i++) {
            Long id = ids.get(i);
            StickerLocal stickerLocal = new StickerLocal();
            stickerLocal.setId(id);
            stickerLocal.setUrl(stickerDbItem.getBaseUrl() + id + "/352b.png");
            stickerIds.add(stickerLocal);
        }
        stickerDbItem.setStickersList(stickerIds);
        return stickerDbItem;
    };
    private String baseUrl;
    private boolean purchased;
    private boolean active;
    private String title;
    private String photo;
    private String background;
    private RealmList<StickerLocal> stickersList;
    @PrimaryKey
    private long id;

    public RealmList<StickerLocal> getStickersList() {
        return stickersList;
    }

    public void setStickersList(RealmList<StickerLocal> stickersList) {
        this.stickersList = stickersList;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
