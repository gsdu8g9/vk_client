package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * @author andreydrobyazko on 4/6/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@RealmClass
public class User extends RealmObject {

    @PrimaryKey
    private long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private int friendRating = 20000;
    /**
     * возвращается, если страница пользователя удалена или заблокирована, содержит значение
     * deleted
     * или banned. Обратите внимание, в этом случае дополнительные поля fields не возвращаются.
     */
    private String deactivated;
    @JsonProperty("hidden")
    /**
     * возвращается при вызове без access_token, если пользователь установил настройку «Кому в
     * интернете видна моя страница» — «Только пользователям ВКонтакте». Обратите внимание, в
     * этом случае дополнительные поля fields не возвращаются.
     * */
    private int hidden;
    @JsonProperty(Fields.photo_id)
    /**
     * id главной фотографии профиля пользователя в формате user_id+photo_id, например,
     * 6492_192164258. В некоторых случаях (если фотография была установлена очень давно) это
     * поле не возвращается.
     * */
    private String photoId;
    /**
     * возвращается 1, если страница пользователя верифицирована, 0 — если не верифицирована.
     */
    private int verified;
    /**
     * возвращается 1, если текущий пользователь находится в черном списке у запрашиваемого.
     */
    private int blacklisted;
    /**
     * пол пользователя. Возможные значения:
     * 1 — женский;
     * 2 — мужской;
     * 0 — пол не указан.
     */
    private int sex;
    @JsonProperty(Fields.bdate)
    /**
     * дата рождения. Возвращается в формате DD.MM.YYYY или DD.MM (если год рождения скрыт). Если
     * дата рождения скрыта целиком, поле отсутствует в ответе.
     * */
    private String dateOfBirth;
    @JsonProperty("home_town")
    private String homeTown;
    private String photo_50;
    private String photo_100;
    private String photo_200_orig;
    private String photo_200;
    private String photo_400_orig;
    private String photo_max;
    private String photo_max_orig;
    /**
     * информация о том, находится ли пользователь сейчас на сайте. Возвращаемые значения: 1 —
     * находится, 0 — не находится. Если пользователь использует мобильное приложение либо
     * мобильную
     * версию сайта, возвращается дополнительное поле online_mobile, содержащее 1. При этом, если
     * используется именно приложение, дополнительно возвращается поле online_app, содержащее его
     * идентификатор.
     */
    private int online;
    private String domain;
    private int has_mobile;
    private String site;
    private String status;
    private int followers_count;
    private int common_count;
    private String nickname;
    private int relation;
    /**
     * 1 – пользователь друг, 2 – пользователь не в друзьях.
     */
    private int is_friend;
    /**
     * статус дружбы с пользователем:
     * 0 – пользователь не является другом,
     * 1 – отправлена заявка/подписка пользователю,
     * 2 – имеется входящая заявка/подписка от пользователя,
     * 3 – пользователь является другом;
     */
    private int friend_status;
    public interface Fields {

        String sex = "sex";
        String bdate = "bdate";
        String country = "country";
        String online = "online";
        String city = "city";
        String photo_50 = "photo_50";
        String photo_100 = "photo_100";
        String photo_200_orig = "photo_200_orig";
        String photo_200 = "photo_200";
        String photo_400_orig = "photo_400_orig";
        String photo_max = "photo_max";
        String photo_max_orig = "photo_max_orig";
        String photo_id = "photo_id";
        String online_mobile = "online_mobile";
        String domain = "domain";
        String has_mobile = "has_mobile";
        String contacts = "contacts";
        String connections = "connections";
        String site = "site";
        String education = "education";
        String universities = "universities";
        String schools = "schools";
        String can_post = "can_post";
        String can_see_all_posts = "can_see_all_posts";
        String can_see_audio = "can_see_audio";
        String can_write_private_message = "can_write_private_message";
        String status = "status";
        String last_seen = "last_seen";
        String common_count = "common_count";
        String relation = "relation";
        String relatives = "relatives";
        String counters = "counters";
        String screen_name = "screen_name";
        String maiden_name = "maiden_name";
        String timezone = "timezone";
        String occupation = "occupation";
        String activities = "activities";
        String interests = "interests";
        String music = "music";
        String movies = "movies";
        String tv = "tv";
        String books = "books";
        String games = "games";
        String about = "about";
        String quotes = "quotes";
        String personal = "personal";
        String friend_status = "friend_status";

    }
    public User() {
    }

    public int getFriendRating() {
        return friendRating;
    }

    public void setFriendRating(int friendRating) {
        this.friendRating = friendRating;
    }

    public int getFriend_status() {
        return friend_status;
    }

    public void setFriend_status(int friend_status) {
        this.friend_status = friend_status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(String deactivated) {
        this.deactivated = deactivated;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public int getBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(int blacklisted) {
        this.blacklisted = blacklisted;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getPhoto_50() {
        return photo_50;
    }

    public void setPhoto_50(String photo_50) {
        this.photo_50 = photo_50;
    }

    public String getPhoto_100() {
        return photo_100;
    }

    public void setPhoto_100(String photo_100) {
        this.photo_100 = photo_100;
    }

    public String getPhoto_200_orig() {
        return photo_200_orig;
    }

    public void setPhoto_200_orig(String photo_200_orig) {
        this.photo_200_orig = photo_200_orig;
    }

    public String getPhoto_200() {
        return photo_200;
    }

    public void setPhoto_200(String photo_200) {
        this.photo_200 = photo_200;
    }

    public String getPhoto_400_orig() {
        return photo_400_orig;
    }

    public void setPhoto_400_orig(String photo_400_orig) {
        this.photo_400_orig = photo_400_orig;
    }

    public String getPhoto_max() {
        return photo_max;
    }

    public void setPhoto_max(String photo_max) {
        this.photo_max = photo_max;
    }

    public String getPhoto_max_orig() {
        return photo_max_orig;
    }

    public void setPhoto_max_orig(String photo_max_orig) {
        this.photo_max_orig = photo_max_orig;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getHas_mobile() {
        return has_mobile;
    }

    public void setHas_mobile(int has_mobile) {
        this.has_mobile = has_mobile;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getCommon_count() {
        return common_count;
    }

    public void setCommon_count(int common_count) {
        this.common_count = common_count;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getIs_friend() {
        return is_friend;
    }

    public void setIs_friend(int is_friend) {
        this.is_friend = is_friend;
    }
}
