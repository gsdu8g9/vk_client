package com.nethergrim.vk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.viewholders.ConversationViewHolder;
import com.nethergrim.vk.inject.Injector;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.web.images.ImageLoader;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * @author andreydrobyazko on 4/6/15.
 */
public class ConversationsAdapter  extends RecyclerView.Adapter<ConversationViewHolder> implements RealmChangeListener {

    @Inject
    ImageLoader il;

    private RealmResults<Conversation> data;
    private Realm realm;

    public ConversationsAdapter(RealmResults<Conversation> data) {
        this.data = data;
        setHasStableIds(true);
        Injector.getInstance().inject(this);
    }

    private void createRealm(Context context){
        realm = Realm.getInstance(context);
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (realm == null){
            createRealm(viewGroup.getContext());
            realm.addChangeListener(this);
        }
        return new ConversationViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vh_conversation, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder conversationViewHolder, int i) {
        Conversation conversation = data.get(i);
        conversationViewHolder.textDetails.setText(conversation.getMessage().getBody());
        conversationViewHolder.textDate.setText(DateUtils.getRelativeTimeSpanString(conversation.getMessage().getDate() * 1000, System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_ALL));

        User user = realm.where(User.class).equalTo("id", conversation.getUser_id()).findFirst();
        if (user != null){
            il.displayUserAvatar(user, conversationViewHolder.imageAvatar);
            conversationViewHolder.textName.setText(user.getFirstName() + " " + user.getLastName());
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getUser_id();
    }

    public void closeRealm(){
        if (realm != null){
            try {
                realm.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onChange() {
        notifyDataSetChanged();
    }
}
