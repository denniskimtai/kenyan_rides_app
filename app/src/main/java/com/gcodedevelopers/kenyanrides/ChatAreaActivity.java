package com.gcodedevelopers.kenyanrides;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gcodedevelopers.kenyanrides.Notifications.APIService;
import com.gcodedevelopers.kenyanrides.Notifications.Client;
import com.gcodedevelopers.kenyanrides.Notifications.Data;
import com.gcodedevelopers.kenyanrides.Notifications.MyResponse;
import com.gcodedevelopers.kenyanrides.Notifications.Sender;
import com.gcodedevelopers.kenyanrides.Notifications.Token;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Services.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAreaActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    FirebaseDatabase db;
    DatabaseReference reference1, reference2;

    TextView chatWithName;

    String chatWith, chatWithPhoneNumber;

    APIService apiService;

    Boolean notify = false;

    String message, userName;

    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<Chat> mChat;

    user user2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_area);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        //get email of person chatting with
        chatWith = getIntent().getStringExtra("chat_with");
        chatWithPhoneNumber = getIntent().getStringExtra("chat_with_phone_number");

        chatWithName = findViewById(R.id.chat_with_name);

        String[] name = chatWith.split("@");

        String cap = name[0].substring(0, 1).toUpperCase() + name[0].substring(1);
        chatWithName.setText(cap);

        recyclerView = findViewById(R.id.messageRecyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //LinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //getting the current user
        user2 = SharedPrefManager.getInstance(ChatAreaActivity.this).getUser();

        readMessage(user2.getMobile_number(), chatWithPhoneNumber);

        db = FirebaseDatabase.getInstance();

        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        String[] parts = user2.getEmail().split("@");
        String[] parts2 = chatWith.split("@");

        sendButton.setOnClickListener(this);


    }

    private void sendNotification(String receiver, String userName, String message){

        user user2 = SharedPrefManager.getInstance(ChatAreaActivity.this).getUser();

        DatabaseReference tokens = db.getReference("messages").child(user2.getMobile_number()).child("Tokens");
        Query query = tokens.orderByKey().equalTo(chatWith);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(user2.getMobile_number(), R.drawable.icon_filter_con, userName+": " + message, "New Message", userName);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(ChatAreaActivity.this, "Failed to send notification", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String sender, String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object>  hashMapSender = new HashMap<>();
        hashMapSender.put("sender", sender);
        hashMapSender.put("receiver", receiver);
        hashMapSender.put("message", message);

        reference.child("Chats").push().setValue(hashMapSender);

    }

    private void readMessage(final String myPhoneNumber, final String userPhoneNumber){

        mChat = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myPhoneNumber) && chat.getSender().equals(userPhoneNumber) ||
                            chat.getReceiver().equals(userPhoneNumber) && chat.getSender().equals(myPhoneNumber)){

                        mChat.add(chat);
                    }

                }
                messageAdapter = new MessageAdapter(ChatAreaActivity.this, mChat);
                recyclerView.setAdapter(messageAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sendButton:

                notify = true;
                String messageText = messageArea.getText().toString();

                //getting the current user
                user user2 = SharedPrefManager.getInstance(ChatAreaActivity.this).getUser();

                if (!messageText.equals("")) {

                    sendMessage(user2.getMobile_number(), chatWithPhoneNumber, messageText);

                }else {
                    Toast.makeText(this, "You can't send an empty message", Toast.LENGTH_SHORT).show();
                }

                messageArea.setText("");

        }
    }


}