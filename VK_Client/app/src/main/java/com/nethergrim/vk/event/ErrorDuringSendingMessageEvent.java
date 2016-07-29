package com.nethergrim.vk.event;


import com.nethergrim.vk.models.WebResponse;

public class ErrorDuringSendingMessageEvent {

    private WebResponse webResponse;

    public ErrorDuringSendingMessageEvent(WebResponse webResponse) {
        this.webResponse = webResponse;
    }

    public WebResponse getWebResponse() {
        return webResponse;
    }
}
