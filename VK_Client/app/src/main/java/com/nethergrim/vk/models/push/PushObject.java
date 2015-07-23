package com.nethergrim.vk.models.push;

/**
 * @author andrej on 23.07.15.
 */
public abstract class PushObject {

    public enum PushType {

//        https://vk.com/dev/push_settings
//
//        msg – личные сообщения. Может принимать массив значений:
//        no_sound – отключить звук;
//        no_text – не передавать текст сообщения.
//        chat – групповые чаты. Может принимать массив значений:
//        no_sound – отключить звук;
//        no_text – не передавать текст сообщения.
//        friend – запрос на добавления в друзья. Может принимать массив значений:
//        mutual – уведомлять только при наличии общих друзей.
//        friend_found – регистрация импортированного контакта;
//        friend_accepted – подтверждение заявки в друзья;
//        reply – ответы;
//        comment – комментарии. Может принимать массив значений:
//        fr_of_fr – уведомления только от друзей и друзей друзей.
//                mention – упоминания. Может принимать массив значений:
//        fr_of_fr – уведомления только от друзей и друзей друзей.
//                like – отметки "Мне нравится". Может принимать массив значений:
//        fr_of_fr – уведомления только от друзей и друзей друзей.
//                repost – действия "Рассказать друзьям". Может принимать массив значений:
//        fr_of_fr – уведомления только от друзей и друзей друзей.
//                wall_post – публикация записи на стене;
//        wall_publish – размещение предложенной новости;
//        group_invite – приглашение в сообщество;
//        group_accepted – подтверждение заявки на вступление в группу;
//        event_soon – ближайшие мероприятия;
//        tag_photo – отметки на фотографиях. Может принимать массив значений:
//        fr_of_fr – уведомления только от друзей и друзей друзей.
//                app_request – запросы в приложениях;
//        sdk_open – установка приложения;
//        new_post – записи выбранных людей и сообществ;
//        birthday – уведомления о днях рождениях на текущую дату.

        Message("msg", "msg", PushMessage.class),
        Friend("friend", "friend", PushFriend.class),
        Other("new_post", "new_post", PushObject.class),
        Call("call", "call", PushCall.class);
        // TODO add more push notifications types


        private String mType;
        private String mCollapseKey;
        private Class mClass;

        PushType(String type, String collapseKey, Class clazz) {
            this.mType = type;
            this.mCollapseKey = collapseKey;
            this.mClass = clazz;
        }

        public String getType() {
            return mType;
        }

        public String getCollapseKey() {
            return mCollapseKey;
        }

        public Class getClazz() {
            return mClass;
        }
    }

    public abstract PushType getPushType();
}
