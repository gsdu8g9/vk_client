package com.nethergrim.vk.web;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.caching.models.Conversation;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.List;

/**
 * @author andreydrobyazko on 4/3/15.
 */
public class WebRequestManagerImpl implements WebRequestManager {

    @Override
    public void getConversations(int limit, int offset, boolean onlyUnread, int previewLenght, final WebCallback<List<Conversation>> callback){
        VKRequest request = new VKRequest(Constants.Requests.MESSAGES_GET_DIALOGS);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                // TODO deserialize json to list of conversations

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
