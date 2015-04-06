package com.nethergrim.vk.web;

import android.util.Log;

import com.kisstools.utils.StringUtil;
import com.nethergrim.vk.Constants;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.inject.Injector;
import com.nethergrim.vk.json.JsonDeserializer;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfUsers;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                ConversationsList result ;
                try {
                    result = mJsonDeserializer.getConversations(response.json.getString("response"));

                    // setting userId and date to every conversation
                    if (result != null){
                        ArrayList<Conversation> conversations = result.getResults();
                        if (conversations != null){
                            for (Conversation conversation : conversations) {
                                conversation.setUser_id(conversation.getMessage().getUser_id());
                                conversation.setDate(conversation.getMessage().getDate());
                            }
                        }
                    }
                    if (callback != null){
                        callback.onResponseSucceed(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    @Override
    public void getUsers(List<Integer> ids, List<String> fields, String nameCase, final WebCallback<ListOfUsers> callback) {
        Map<String, Object> params = new HashMap<>();

        if (ids != null){
            if (ids.size() > 1000){
                throw  new IllegalArgumentException("you want to fetch too much users. Max is 1000");
            }

            StringBuilder sb = new StringBuilder();
            for (Integer id : ids) {
                sb.append(id);
                sb.append(", ");
            }
            String idsValues = StringUtil.cutText(sb.toString(), sb.toString().length() - 2);
            Log.e("TAG", "ids: " + idsValues);
            params.put("user_ids", idsValues);
        }

        if (fields != null){
            StringBuilder sb = new StringBuilder();
            for (String field : fields) {
                sb.append(field);
                sb.append(", ");
            }
            String idsValues = StringUtil.cutText(sb.toString(), sb.toString().length() - 2);
            Log.e("TAG", "fields: " + idsValues);
            params.put("fields", idsValues);
        }

        VKRequest vkRequest = new VKRequest(Constants.Requests.GET_USERS, new VKParameters(params));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);



                // TODo
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
