package com.nethergrim.vk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author andrej on 16.08.15.
 */
public class StartupResponse {


    @JsonProperty("response")
    private StartupResponseObject response;

    public StartupResponseObject getResponse() {
        return response;
    }

    public void setResponse(StartupResponseObject response) {
        this.response = response;
    }

    public static class StartupResponseObject {


        @JsonProperty("me")
        private User me;

        @JsonProperty("gcm")
        private String gcm;
        @JsonProperty("online")
        private String online;

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public String getGcm() {
            return gcm;
        }

        public void setGcm(String gcm) {
            this.gcm = gcm;
        }

        public User getMe() {
            return me;
        }

        public void setMe(User me) {
            this.me = me;
        }

        @Override
        public String toString() {
            return "=============================\nstartup response: \ngcm: " + gcm + "\nonline: "
                    + online + "\nme: " + me.toString();
        }
    }
}
