package com.nethergrim.vk.web;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.inject.Injector;
import com.nethergrim.vk.json.JsonDeserializer;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author andreydrobyazko on 4/3/15.
 */
public class WebRequestManagerImpl implements WebRequestManager {

    @Inject
    JsonDeserializer mJsonDeserializer;

    public WebRequestManagerImpl() {
        Injector.getInstance().inject(this);
    }

    @Override
    public void getConversations(int limit, int offset, boolean onlyUnread, int previewLenght, final WebCallback<ConversationsList> callback){
        Map<String, Object> params = new HashMap<>();
        if (offset > 0){
            params.put("offset", offset);
        }
        if (limit != 0){
            params.put("count", limit);
        }
        if (onlyUnread){
            params.put("unread", 1);
        }
        if (previewLenght > 0){
            params.put("preview_length", previewLenght);
        }
        VKRequest request = new VKRequest(Constants.Requests.MESSAGES_GET_DIALOGS, new VKParameters(params));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                ConversationsList result = mJsonDeserializer.getConversations(response.responseString);
                if (callback != null){
                    callback.onResponseSucceed(result);
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                if (callback != null){
                    callback.onResponseFailed(error);
                }
            }
        });

    }


}
