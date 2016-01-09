package com.matejhacin.ispogovor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by matejhacin on 09/01/16.
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    /*
    Variables
     */

    ArrayList<Message> messageList;
    Context context;

    /*
    Contructor
     */

    public MessageRecyclerAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    /*
    Callbacks
     */

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getUsername().equals(LoginManager.getInstance(context).getUsername())) return MessageViewType.ME;
        return MessageViewType.OTHER;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MessageViewType.ME) return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_me, parent, false));
        return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_other, parent, false));
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.nameTextView.setText(messageList.get(position).getUsername());
        holder.contentTextView.setText(messageList.get(position).getText());
    }

    /*
    Methods
     */

    public void updateData(ArrayList<Message> messageList) {
        this.messageList = messageList;
        this.notifyDataSetChanged();
    }
}

class MessageViewType{
    public static int ME = 0;
    public static int OTHER = 1;
}