package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;

/**
 * @author andrej on 04.08.15.
 *         комментарий к записи на стене;
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WallReply extends RealmObject {


    /**
     * идентификатор комментария
     */
    @JsonProperty("cid")
    private long commentId;

    /**
     * идентификатор автора комментария;
     */
    @JsonProperty("uid")
    private long uid;


    /**
     * идентификатор автора комментария;
     */
    @JsonProperty("from_id")
    private long from_id;

    /**
     * дата создания комментария в формате unixtime;
     */
    @JsonProperty("date")
    private long date;

    /**
     * текст комментария;
     */
    @JsonProperty("text")
    private String text;

    /**
     * информация о лайках к комментарию
     */
    @JsonProperty("likes")
    private LikesDescription likes;
    /**
     * идентификатор пользователя, в ответ которому был оставлен комментарий
     */
    @JsonProperty("reply_to_uid")
    private long replyToUid;
    /**
     * идентификатор комментария, в ответ на который был оставлен текущий;
     */
    @JsonProperty("reply_to_cid")
    private long replyToCId;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getFrom_id() {
        return from_id;
    }

    public void setFrom_id(long from_id) {
        this.from_id = from_id;
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

    public LikesDescription getLikes() {
        return likes;
    }

    public void setLikes(LikesDescription likes) {
        this.likes = likes;
    }

    public long getReplyToUid() {
        return replyToUid;
    }

    public void setReplyToUid(long replyToUid) {
        this.replyToUid = replyToUid;
    }

    public long getReplyToCId() {
        return replyToCId;
    }

    public void setReplyToCId(long replyToCId) {
        this.replyToCId = replyToCId;
    }

}
