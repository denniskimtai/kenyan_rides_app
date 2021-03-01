package com.gcodedevelopers.kenyanrides;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import Services.Utils;

public class ChatAreaActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    FirebaseDatabase db;
    DatabaseReference reference1, reference2;

    TextView chatWithName;

    String chatWith, chatWithPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_area);

        //get email of person chatting with
        chatWith = getIntent().getStringExtra("chat_with");
        chatWithPhoneNumber = getIntent().getStringExtra("chat_with_phone_number");

        chatWithName = findViewById(R.id.chat_with_name);

        String[] name = chatWith.split("@");

        String cap = name[0].substring(0, 1).toUpperCase() + name[0].substring(1);
        chatWithName.setText(cap);

        //getting the current user
        user user2 = SharedPrefManager.getInstance(ChatAreaActivity.this).getUser();

        db = FirebaseDatabase.getInstance();

        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        String[] parts = user2.getEmail().split("@");
        String[] parts2 = chatWith.split("@");

        reference1 = db.getReference("messages").child(user2.getMobile_number()).child(user2.getMobile_number() + ":" + parts[0].replace(".", "**")  + "_" + chatWithPhoneNumber + ":" + parts2[0].replace(".","**"));
        reference2 = db.getReference("messages").child(chatWithPhoneNumber).child(chatWithPhoneNumber + ":" + parts2[0].replace(".","**") + "_" + user2.getMobile_number() + ":" + parts[0].replace(".", "**"));

        sendButton.setOnClickListener(this);

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                String cap = chatWith.substring(0, 1).toUpperCase() + chatWith.substring(1);

                if(userName.equals(user2.getEmail())){
                    addMessageBox("You:\n" + message, 1);
                }
                else{
                    addMessageBox( cap+ ":\n" + message, 2);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

                String messageText = messageArea.getText().toString();
                messageArea.getText().clear();

                //getting the current user
                user user2 = SharedPrefManager.getInstance(ChatAreaActivity.this).getUser();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", user2.getEmail());

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                    break;

                }

        }
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatAreaActivity.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(15, 10, 15, 5);
        textView.setLayoutParams(lp);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.sent_message_bubble);
            textView.setTextColor(Color.parseColor("#ffffff"));
        }
        else{
            textView.setBackgroundResource(R.drawable.received_message_bubble);
            textView.setTextColor(Color.parseColor("#ffffff"));
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

}