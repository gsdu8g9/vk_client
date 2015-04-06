package com.nethergrim.vk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.viewholders.ConversationViewHolder;
import com.nethergrim.vk.models.Conversation;

import io.realm.RealmResults;

/**
 * @author andreydrobyazko on 4/6/15.
 */
public class ConversationsAdapter  extends RecyclerView.Adapter<ConversationViewHolder> {

    private RealmResults<Conversation> data;

    public ConversationsAdapter(RealmResults<Conversation> data) {
        this.data = data;
        setHasStableIds(true);
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ConversationViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vh_conversation, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder conversationViewHolder, int i) {
        Conversation conversation = data.get(i);


        conversationViewHolder.textDetails.setText(conversation.getMessage().getBody());
        conversationViewHolder.textDate.setText(String.valueOf(conversation.getMessage().getDate()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getUser_id();
    }


}
