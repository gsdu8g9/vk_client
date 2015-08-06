package com.nethergrim.vk.web;

import android.os.Build;
import android.util.Log;

import com.kisstools.utils.StringUtil;
import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.callbacks.WebCallback;
import com.nethergrim.vk.images.ImageLoader;
import com.nethergrim.vk.images.PaletteProvider;
import com.nethergrim.vk.json.JsonDeserializer;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.ConversationsList;
import com.nethergrim.vk.models.ListOfFriendIds;
import com.nethergrim.vk.models.ListOfMessages;
import com.nethergrim.vk.models.ListOfUsers;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.ConversationUtils;
import com.nethergrim.vk.utils.UserUtils;
import com.nethergrim.vk.utils.Utils;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author andreydrobyazko on 4/3/15.
 */
public class WebRequestManagerImpl implements WebRequestManager {

    public static final String TAG = WebRequestManagerImpl.class.getSimpleName();
    @Inject
    JsonDeserializer mJsonDeserializer;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    PaletteProvider mPaletteProvider;

    public WebRequestManagerImpl() {
        MyApplication.getInstance().getMainComponent().inject(this);
    }

    @Override
    public void getConversations(int limit,
            int offset,
            boolean onlyUnread,
            int previewLenght,
            final WebCallback<ConversationsList> callback) {
        Map<String, Object> params = new HashMap<>();
        if (offset > 0) {
            params.put("offset", offset);
        }
        if (limit != 0) {
            params.put("count", limit);
        }
        if (onlyUnread) {
            params.put("unread", 1);
        }
        if (previewLenght > 0) {
            params.put("preview_length", previewLenght);
        }
        VKRequest request = new VKRequest(Constants.Requests.MESSAGES_GET_DIALOGS,
                new VKParameters(params));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                ConversationsList result;
                try {
                    result = mJsonDeserializer.getConversations(
                            response.json.getString("response"));
                    JSONArray conversationsArray = response.json.getJSONObject("response")
                            .getJSONArray("items");
                    // setting userId and date to every conversation

                    if (result != null) {
                        ArrayList<Conversation> conversations = result.getResults();
                        if (conversations != null) {
                            int i = 0;
                            for (Conversation conversation : conversations) {
                                if (ConversationUtils.isConversationAGroupChat(conversation)) {
                                    JSONObject jsonConversation = conversationsArray.getJSONObject(
                                            i).getJSONObject("message");
                                    JSONArray chatActiveArray = jsonConversation.getJSONArray(
                                            "chat_active");
                                    String lastId = chatActiveArray.getString(0);
                                    long from_id = Long.valueOf(lastId);
                                    conversation.getMessage().setFrom_id(from_id);
                                    conversation.setId(conversation.getMessage().getChat_id());
                                } else {
                                    conversation.getMessage()
                                            .setFrom_id(conversation.getMessage().getUser_id());
                                    conversation.setId(conversation.getMessage().getUser_id());
                                }
                                conversation.setDate(conversation.getMessage().getDate());
                                i++;
                            }
                        }
                    }
                    if (callback != null) {
                        callback.onResponseSucceed(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                if (callback != null) {
                    callback.onResponseFailed(error);
                }
            }
        });

    }

    @Override
    public void getUsers(List<Long> ids,
            List<String> fields,
            String nameCase,
            final WebCallback<ListOfUsers> callback) {
        Map<String, Object> params = new HashMap<>();

        if (ids != null) {
            if (ids.size() > 1000) {
                throw new IllegalArgumentException("you want to fetch too much users. Max is 1000");
            }

            StringBuilder sb = new StringBuilder();
            for (Long id : ids) {
                sb.append(id);
                sb.append(", ");
            }
            String idsValues = StringUtil.cutText(sb.toString(), sb.toString().length() - 2);
            params.put("user_ids", idsValues);
        }

        if (fields != null) {
            StringBuilder sb = new StringBuilder();
            for (String field : fields) {
                sb.append(field);
                sb.append(", ");
            }
            String idsValues = StringUtil.cutText(sb.toString(), sb.toString().length() - 2);
            params.put("fields", idsValues);
        }

        VKRequest vkRequest = new VKRequest(Constants.Requests.GET_USERS, new VKParameters(params));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                ListOfUsers listOfUsers = mJsonDeserializer.getListOfUsers(response.responseString);
                if (listOfUsers != null && listOfUsers.getResponse() != null && callback != null) {
                    for (User user : listOfUsers.getResponse()) {
                        mImageLoader.cacheUserAvatars(user);
                    }
                    mPaletteProvider.generateAndStorePalette(listOfUsers.getResponse());
                    callback.onResponseSucceed(listOfUsers);
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                if (callback != null) {
                    callback.onResponseFailed(error);
                }
            }
        });
    }

    @Override
    public void getUsers(List<Long> ids, final WebCallback<ListOfUsers> callback) {
        getUsers(ids, UserUtils.getDefaultUserFields(), null, callback);

    }

    @Override
    public void getUsersForConversations(ConversationsList list,
            WebCallback<ListOfUsers> callback) {
        if (list != null && list.getResults() != null) {
            List<Long> ids = new ArrayList<>(list.getResults().size());
            for (Conversation conversation : list.getResults()) {
                if (ConversationUtils.isConversationAGroupChat(conversation)) {
                    ids.add(conversation.getMessage().getFrom_id());
                    ids.add(conversation.getMessage().getUser_id());
                } else {
                    ids.add(conversation.getId());
                }
            }
            getUsers(ids, UserUtils.getDefaultUserFields(),
                    null, callback);
        }
    }

    @Override
    public void getCurrentUser(final WebCallback<User> callback) {
        Map<String, Object> params = new HashMap<>();

        params.put("fields", UserUtils.getDefaultUserFieldsAsString());

        VKRequest vkRequest = new VKRequest(Constants.Requests.GET_USERS, new VKParameters(params));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                ListOfUsers listOfUsers = mJsonDeserializer.getListOfUsers(response.responseString);
                if (listOfUsers != null && listOfUsers.getResponse() != null
                        && !listOfUsers.getResponse().isEmpty()) {

                    User user = listOfUsers.getResponse().get(0);
                    if (user != null && callback != null) {
                        mImageLoader.cacheUserAvatars(user);
                        callback.onResponseSucceed(user);
                    }
                }

            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                if (callback != null) {
                    callback.onResponseFailed(error);
                }
            }
        });
    }

    @Override
    public void registerToPushNotifications(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("device_model", "android");
        params.put("device_id", Utils.generateAndroidId());
        params.put("system_version", String.valueOf(Build.VERSION.SDK_INT));
        // TODO fix settings
        params.put("settings", "{\"msg\":\"on\", \"chat\":[\"no_sound\",\"no_text\"], "
                + "\"friend\":\"on\", \"reply\":\"on\", \"mention\":\"fr_of_fr\"} ");
        VKRequest vkRequest = new VKRequest(Constants.Requests.ACCOUNT_REGISTER_DEVICE,
                new VKParameters(params));
        vkRequest.setRequestListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.e("TAG", "GCM register ok: \n" + response.responseString);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.e("TAG", "GCM register error: " + error.errorMessage + " " + error.apiError);
            }
        });
        vkRequest.start();
    }

    @Override
    public void unregisterFromPushNotifications() {
        Map<String, Object> params = new HashMap<>();
        params.put("device_id", Utils.generateAndroidId());
        VKRequest vkRequest = new VKRequest(Constants.Requests.ACCOUNT_UNREGISTER_DEVICE,
                new VKParameters(params));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.e("TAG", "unregistered GCM ok: " + response.responseString);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.e("TAG", "unregister error: " + error.errorMessage);
            }
        });
    }

    @Override
    public void getFriendsList(long userId, final WebCallback<ListOfFriendIds> callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", String.valueOf(userId));
        params.put("order", "random");

        VKRequest vkRequest = new VKRequest(Constants.Requests.FRIENDS_GET,
                new VKParameters(params));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                ListOfFriendIds listOfFriendIds = null;
                try {
                    listOfFriendIds = mJsonDeserializer.getFriendsIds(
                            response.json.getJSONObject("response").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (callback != null) {
                    callback.onResponseSucceed(listOfFriendIds);
                }

            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.e("TAG", "error: " + error.toString());
                if (callback != null) {
                    callback.onResponseFailed(error);
                }
            }
        });
    }

    @Override
    public void registerOnline() {
        Map<String, Object> params = new HashMap<>();
        params.put("voip", String.valueOf(0));
        VKRequest vkRequest = new VKRequest(Constants.Requests.ACCOUNT_SETONLINE,
                new VKParameters(params));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(TAG, "register online ok, result: " + response.responseString);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.e(TAG, "register online error: " + error.toString());
            }
        });
    }

    @Override
    public void getChatHistory(int offset,
            int count,
            long userId,
            long chatId,
            long startMessageId,
            boolean chronologicalOrder, final WebCallback<ListOfMessages> callback) {

        Map<String, Object> params = new HashMap<>();
        if (offset >= 0) {
            params.put("offset", String.valueOf(offset));
        }
        if (count > 0 && count <= 200) {
            params.put("count", String.valueOf(count));
        }

        if (userId > 0) {
            params.put("user_id", String.valueOf(userId));
        }
        if (chatId > 0) {
            params.put("chat_id", String.valueOf(chatId));
        }
        if (startMessageId > 0) {
            params.put("start_message_id", String.valueOf(startMessageId));
        } else {
            if (chronologicalOrder) {
                params.put("rev", "1");
            }
        }

        VKRequest vkRequest = new VKRequest(Constants.Requests.MESSAGES_GET_HISTORY,
                new VKParameters(params));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                ListOfMessages result;
                try {
                    result = mJsonDeserializer.getListOfMessages(
                            response.json.getString("response"));
                    if (callback != null) {
                        callback.onResponseSucceed(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                if (callback != null) {
                    callback.onResponseFailed(error);
                }
            }
        });

    }

}
