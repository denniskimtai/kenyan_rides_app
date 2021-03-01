package com.gcodedevelopers.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private DatabaseReference chatsRef;

    private List<chatsData> chatsDataList;
    private RecyclerView rv;
    private ChatsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        chatsRef = FirebaseDatabase.getInstance().getReference("messages");

        rv=(RecyclerView)findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        chatsDataList = new ArrayList<>();

        //getting the current user
        user user2 = SharedPrefManager.getInstance(ChatsActivity.this).getUser();

        DatabaseReference userChatRef = chatsRef.child(user2.getMobile_number());

        userChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot chatsSnapshot : dataSnapshot.getChildren()) {
                        //Log.i(TAG, zoneSnapshot.child("ZNAME").getValue(String.class));

                        String[] parts = chatsSnapshot.getKey().split("_");
                        String[] parts2 = parts[1].split(":");

                        chatsData chatsData = new chatsData(parts2[1], parts2[0]);
                        chatsDataList.add(chatsData);

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



    }
}