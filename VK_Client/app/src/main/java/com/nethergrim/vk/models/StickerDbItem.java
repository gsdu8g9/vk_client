package com.nethergrim.vk.models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import rx.functions.Func1;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 15.10.15.
 */
public class StickerDbItem extends RealmObject {

    private String baseUrl;
    private boolean purchased;
    private boolean active;
    private String title;
    private String photo;
    private String background;
    private RealmList<RealmLong> stickerIds;
    private RealmList<RealmString> urls;
    @PrimaryKey
    private long id;
    public static final Func1<StockItem, StickerDbItem> MAPPER
            = stockItem -> {
        StickerDbItem stickerDbItem = new StickerDbItem();
        stickerDbItem.setBaseUrl(stockItem.getProduct().getStickers().getBaseUrl());
        stickerDbItem.setActive(stockItem.getProduct().getActive() == 1);
        stickerDbItem.setPurchased(stockItem.getProduct().getPurchased() == 1);
        stickerDbItem.setTitle(stockItem.getProduct().getTitle());

        stickerDbItem.setId(stockItem.getProduct().getId());
        stickerDbItem.setPhoto(stockItem.getProduct().getBaseUrl() + "thumb_102.png");
        stickerDbItem.setBackground(stockItem.getBackground());
        List<Long> ids = stockItem.getProduct().getStickers().getStickerIds();
        RealmList<RealmLong> stickerIds = new RealmList<>();
        RealmList<RealmString> urls = new RealmList<>();

        for (int i = 0, size = ids.size(); i < size; i++) {
            Long id = ids.get(i);
            stickerIds.add(new RealmLong(id));
            urls.add(new RealmString(stickerDbItem.getBaseUrl() + id + "/352b.png"));
        }

        stickerDbItem.setStickerIds(stickerIds);
        stickerDbItem.setUrls(urls);


        return stickerDbItem;
    };

    public RealmList<RealmString> getUrls() {
        return urls;
    }

    public void setUrls(RealmList<RealmString> urls) {
        this.urls = urls;
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

    public RealmList<RealmLong> getStickerIds() {
        return stickerIds;
    }

    public void setStickerIds(RealmList<RealmLong> stickerIds) {
        this.stickerIds = stickerIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
