package com.gcodedevelopers.kenyanrides;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> {

    public Context mContext;
    private List<chatsData> chatsDataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView initialText, nameText;
        public LinearLayout chatLayout;

        public MyViewHolder(View view) {
            super(view);
            initialText = view.findViewById(R.id.initialText);
            nameText =  view.findViewById(R.id.nameText);
            chatLayout = view.findViewById(R.id.chatLayout);

        }

    }


    public ChatsAdapter(Context mContext, List<chatsData> chatsDataList) {
        this.mContext = mContext;
        this.chatsDataList = chatsDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chats_layout, parent, false);

        return new ChatsAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        chatsData chatsData = chatsDataList.get(position);

        String cap = chatsData.getChat_with().substring(0, 1).toUpperCase() + chatsData.getChat_with().substring(1);

        holder.nameText.setText(cap);
        holder.initialText.setText(chatsData.getChat_with().substring(0,1));
        holder.initialText.setAllCaps(true);


        holder.chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //go to chat area activity
                Intent intent = new Intent(mContext, ChatAreaActivity.class);
                intent.putExtra("chat_with", chatsData.getChat_with());
                intent.putExtra("chat_with_phone_number", chatsData.getChat_with_phone_number());
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return chatsDataList.size();
    }


}
