package com.nethergrim.vk.models.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nethergrim.vk.models.WebResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendMessageResponse extends WebResponse {

    @JsonProperty("response")
    private long response;

    private long peerId;
    private long randomId;

    public long getPeerId() {
        return peerId;
    }

    public void setPeerId(long peerId) {
        this.peerId = peerId;
    }

    public long getRandomId() {
        return randomId;
    }

    public void setRandomId(long randomId) {
        this.randomId = randomId;
    }

    public long getResponse() {
        return response;
    }

    public void setResponse(long response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SendMessageResponse that = (SendMessageResponse) o;

        return response == that.response;

    }

    @Override
    public int hashCode() {
        return (int) (response ^ (response >>> 32));
    }

    @Override
    public String toString() {
        return "SendMessageResponse{" +
                "response=" + response +
                '}';
    }
}
