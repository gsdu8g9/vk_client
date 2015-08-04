package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * @author andrej on 04.08.15.
 *         Запись на стене (type = wall)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wall extends RealmObject {

    /**
     * идентификатор записи на стене;
     */
    private long id;
    /**
     * идентификатор владельца записи;
     */
    @JsonProperty("to_id")
    private long toId;
    /**
     *
     * */
    @JsonProperty("from_id")
    private long fromId;
    /**
     * время публикации записи в формате unixtime;
     */
    @JsonProperty("date")
    private long date;
    /**
     * текст записи;
     */
    @JsonProperty("text")
    private String text;
    /**
     * информация о комментариях к записи;
     */
    @JsonProperty("comments")
    private CommentsDescription comments;
    /**
     * информация о лайках к записи
     */
    @JsonProperty("likes")
    private LikesDescription likes;
    /**
     * информация о репостах записи («Рассказать друзьям»)
     */
    @JsonProperty("reposts")
    private RepostsDescription reposts;
    /**
     * содержит массив объектов, прикреплённых к текущей записи (фотография, ссылка и
     * т.п.). Более подробная информация представлена на странице Описание формата вложений в
     * записях на стене.
     */
    @JsonProperty("attachments")
    private RealmList<Attachment> attachments;
    /**
     * информация о месте (если доступно);
     */
    @JsonProperty("geo")
    private Geo geo;
    /**
     * информация о том, как была создана запись;
     */
    @JsonProperty("post_source")
    private PostSource postSource;
    /**
     * если запись была опубликована от имени группы и подписана пользователем, то в
     * поле содержится идентификатор её автора;
     */
    @JsonProperty("signer_id")
    private long signerId;
    /**
     * если запись является копией записи с чужой стены, то в поле содержится
     * идентификатор владельца стены, у которого была скопирована запись;
     */
    @JsonProperty("copy_owner_id")
    private long copyOwnerId;
    /**
     * если запись является копией записи с чужой стены, то в поле содержится
     * идентификатор скопированной записи на стене ее владельца;
     */
    @JsonProperty("copy_post_id")
    private long copyPostId;
    /**
     * если запись является копией записи с чужой стены и при её копировании был
     * добавлен комментарий, его текст содержится в данном поле.
     */
    @JsonProperty("copy_text")
    private String copyText;

    public PostSource getPostSource() {
        return postSource;
    }

    public void setPostSource(PostSource postSource) {
        this.postSource = postSource;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CommentsDescription getComments() {
        return comments;
    }

    public void setComments(CommentsDescription comments) {
        this.comments = comments;
    }

    public LikesDescription getLikes() {
        return likes;
    }

    public void setLikes(LikesDescription likes) {
        this.likes = likes;
    }

    public RepostsDescription getReposts() {
        return reposts;
    }

    public void setReposts(RepostsDescription reposts) {
        this.reposts = reposts;
    }

    public RealmList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(RealmList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public long getSignerId() {
        return signerId;
    }

    public void setSignerId(long signerId) {
        this.signerId = signerId;
    }

    public long getCopyOwnerId() {
        return copyOwnerId;
    }

    public void setCopyOwnerId(long copyOwnerId) {
        this.copyOwnerId = copyOwnerId;
    }

    public long getCopyPostId() {
        return copyPostId;
    }

    public void setCopyPostId(long copyPostId) {
        this.copyPostId = copyPostId;
    }

    public String getCopyText() {
        return copyText;
    }

    public void setCopyText(String copyText) {
        this.copyText = copyText;
    }

}
