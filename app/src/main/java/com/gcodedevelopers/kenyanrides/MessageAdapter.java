package com.gcodedevelopers.kenyanrides;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private static final int MSG_TPYE_LEFT = 0;
    private static final int MSG_TPYE_RIGHT = 1;

    public Context mContext;
    private List<Chat> mChat;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView showMessage;
        public ImageView profileImage;

        public MyViewHolder(View view) {
            super(view);
            showMessage = view.findViewById(R.id.show_message);
            profileImage = view.findViewById(R.id.profile_image);

        }

    }


    public MessageAdapter(Context mContext, List<Chat> mChat) {
        this.mContext = mContext;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TPYE_RIGHT) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_right, parent, false);

            return new MessageAdapter.MyViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_left, parent, false);

            return new MessageAdapter.MyViewHolder(itemView);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Chat chat = mChat.get(position);

        holder.showMessage.setText(chat.getMessage());

        holder.profileImage.setImageResource(R.drawable.logo);

    }


    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {

        //getting the current user
        user user2 = SharedPrefManager.getInstance(mContext).getUser();
        if (mChat.get(position).getSender().equals(user2.getMobile_number())){
            return MSG_TPYE_RIGHT;
        }else {
            return MSG_TPYE_LEFT;
        }

    }
}

