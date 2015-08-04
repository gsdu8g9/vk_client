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

}
