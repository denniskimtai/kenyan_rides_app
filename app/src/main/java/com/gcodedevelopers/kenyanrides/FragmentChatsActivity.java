package com.gcodedevelopers.kenyanrides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentChatsActivity extends Fragment {

    private DatabaseReference chatsRef;

    private List<chatsData> chatsDataList;
    private RecyclerView rv;
    private ChatsAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_chats_activity, null);

        chatsRef = FirebaseDatabase.getInstance().getReference("messages");

        rv = myView.findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatsDataList = new ArrayList<>();

        //getting the current user
        user user2 = SharedPrefManager.getInstance(getActivity()).getUser();

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

                    adapter = new ChatsAdapter(getActivity(), chatsDataList);
                    rv.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "onCancelled", databaseError.toException());

                Toast.makeText(getActivity(), "Database error fetching your chats " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return myView;

    }
}
