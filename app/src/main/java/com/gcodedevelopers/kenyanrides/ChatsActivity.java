package com.gcodedevelopers.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gcodedevelopers.kenyanrides.Notifications.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private DatabaseReference chatsRef;

    private List<chatsData> chatsDataList;
    private RecyclerView rv;
    private ChatsAdapter adapter;

    private List<String> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        chatsRef = FirebaseDatabase.getInstance().getReference("Chats");

        rv = (RecyclerView)findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        usersList = new ArrayList<>();

        //getting the current user
        user user2 = SharedPrefManager.getInstance(ChatsActivity.this).getUser();

        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    for (DataSnapshot chatsSnapshot : dataSnapshot.getChildren()) {

                        Chat chat = chatsSnapshot.getValue(Chat.class);

                        if (chat.getSender().equals(user2.getMobile_number())){

                            chatsData chatsData = new chatsData(chat.getReceiver(), "receiver");
                            chatsDataList.add(chatsData);

                        }

                        if (chat.getReceiver().equals(user2.getMobile_number())){

                            chatsData chatsData = new chatsData(chat.getSender(), "sender");
                            chatsDataList.add(chatsData);

                        }

                    }

                    adapter = new ChatsAdapter(ChatsActivity.this, chatsDataList);
                    rv.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "onCancelled", databaseError.toException());

                Toast.makeText(ChatsActivity.this, "Database error fetching your chats " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void updateToken(String token){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages");
        Token token1 = new Token(token);
        //getting the current user
        user user2 = SharedPrefManager.getInstance(ChatsActivity.this).getUser();

        reference.child(user2.getMobile_number()).child("Tokens").setValue(token1);

    }

}