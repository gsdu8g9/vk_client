package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nethergrim.vk.utils.ConversationUtils;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 3/20/15.
 */
@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@RealmClass
public class Message implements RealmModel {

    @PrimaryKey
    /**
     * идентификатор сообщения (не возвращается для пересланных сообщений).
     * */
    private long id;

    /**
     * идентификатор пользователя, в диалоге с которым находится сообщение.
     */
    private long user_id;

    /**
     * идентификатор автора сообщения.
     */
    private long from_id;
    /**
     * дата отправки сообщения в формате unixtime.
     * положительное число
     */
    private long date;

    /**
     * статус сообщения (0 — не прочитано, 1 — прочитано, не возвращается для пересланных
     * сообщений).
     */
    private int read_state = 1;

    /**
     * тип сообщения (0 — полученное, 1 — отправленное, не возвращается для пересланных сообщений).
     */
    @JsonProperty("out")
    private int out;
    /**
     * заголовок сообщения или беседы.
     */
    private String title;
    /**
     * заголовок сообщения или беседы.
     */
    private String body;
    /**
     * информация о местоположении
     */
    private Geo geo;
    /**
     * содержатся ли в сообщении emoji-смайлы.
     */
    private int emoji;
    /**
     * является ли сообщение важным.
     */
    private int important;
    /**
     * удалено ли сообщение.
     */
    private int deleted;
    /**
     * массив медиа-вложений (см. Описание формата медиа-вложений).
     */
    private RealmList<Attachment> attachments;
    /**
     * массив пересланных сообщений (если есть).
     */
    private RealmList<Message> fwd_messages;
    /**
     * Айди чата (group conversation).
     * идентификатор беседы.
     */
    private long chat_id;
    /**
     * настройки уведомлений для беседы, если они есть.
     * sound и disabled_until
     */
    private PushSettings push_settings;
    /**
     * идентификатор создателя беседы.
     * положительное число
     */
    private int admin_id;
    /**
     * количество участников беседы.
     * положительное число
     */
    private int users_count;
    /**
     * поле передано, если это служебное сообщение
     * строка, может быть chat_photo_update или chat_photo_remove, а с версии 5.14 еще и
     * chat_create, chat_title_update, chat_invite_user, chat_kick_user
     */
    private String action;
    /**
     * идентификатор пользователя (если > 0) или email (если < 0), которого пригласили или
     * исключили
     * число, для служебных сообщений с action равным chat_invite_user или chat_kick_user
     */
    private long action_mid;
    /**
     * email, который пригласили или исключили
     * строка, для служебных сообщений с action равным chat_invite_user или chat_kick_user и
     * отрицательным action_mid
     */
    private String action_email;
    /**
     * название беседы
     * строка, для служебных сообщений с action равным chat_create или chat_title_update
     */
    private String action_text;
    /**
     * url копии фотографии беседы шириной 50px.
     * строка
     */
    private String photo_50;
    /**
     * url копии фотографии беседы шириной 100px.
     * строка
     */
    private String photo_100;
    /**
     * url копии фотографии беседы шириной 200px.
     * строка
     */
    private String photo_200;

    /**
     * Local only!
     * Indicates if this message is pending
     */
    private boolean pending = false;

    @Ignore
    @JsonProperty("chat_active")
    private List<Long> chat_active;

    @Ignore
    private long authorId;

    public Message() {
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public List<Long> getChat_active() {
        return chat_active;
    }

    public void setChat_active(List<Long> chat_active) {
        this.chat_active = chat_active;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    public RealmList<Message> getFwd_messages() {
        return fwd_messages;
    }

    public void setFwd_messages(RealmList<Message> fwd_messages) {
        this.fwd_messages = fwd_messages;
    }

    public RealmList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(RealmList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getPhoto_200() {
        return photo_200;
    }

    public void setPhoto_200(String photo_200) {
        this.photo_200 = photo_200;
    }

    public String getPhoto_100() {
        return photo_100;
    }

    public void setPhoto_100(String photo_100) {
        this.photo_100 = photo_100;
    }

    public String getPhoto_50() {
        return photo_50;
    }

    public void setPhoto_50(String photo_50) {
        this.photo_50 = photo_50;
    }

    public String getAction_text() {
        return action_text;
    }

    public void setAction_text(String action_text) {
        this.action_text = action_text;
    }

    public String getAction_email() {
        return action_email;
    }

    public void setAction_email(String action_email) {
        this.action_email = action_email;
    }

    public long getAction_mid() {
        return action_mid;
    }

    public void setAction_mid(long action_mid) {
        this.action_mid = action_mid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getUsers_count() {
        return users_count;
    }

    public void setUsers_count(int users_count) {
        this.users_count = users_count;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public PushSettings getPush_settings() {
        return push_settings;
    }

    public void setPush_settings(PushSettings push_settings) {
        this.push_settings = push_settings;
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public int isRead_state() {
        return read_state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int isEmoji() {
        return emoji;
    }

    public int isImportant() {
        return important;
    }

    public int isDeleted() {
        return deleted;
    }

    public int getRead_state() {
        return read_state;
    }

    public void setRead_state(int read_state) {
        this.read_state = read_state;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public int getEmoji() {
        return emoji;
    }

    public void setEmoji(int emoji) {
        this.emoji = emoji;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isAGroupChat(){
        return getChat_id() > 0;
    }

    public long getPeerId(){

        if (isAGroupChat()){
            return getChat_id() + ConversationUtils.GROUP_CHAT_ADDITIONAL_VALUE;
        }
        return getUser_id();
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public long getConversationId(){
        if (isAGroupChat()){
            return getChat_id();
        }
        return getUser_id();
    }
}
