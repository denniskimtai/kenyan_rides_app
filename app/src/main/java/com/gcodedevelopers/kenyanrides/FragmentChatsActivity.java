package com.gcodedevelopers.kenyanrides;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

        //getting the current user
        user user2 = SharedPrefManager.getInstance(getActivity()).getUser();

        chatsRef = FirebaseDatabase.getInstance().getReference("Chats");

        rv = myView.findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatsDataList = new ArrayList<>();



        chatsRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                chatsDataList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot chatsSnapshot : dataSnapshot.getChildren()) {

                        Chat chat = chatsSnapshot.getValue(Chat.class);

                        if (chat.getSender().equals(user2.getMobile_number())){

                            chatsData chatsData = new chatsData(chat.getReceiver(), chat.getReceiver());

                            chatsDataList.add(chatsData);

                        }

                        if (chat.getReceiver().equals(user2.getMobile_number())){

                            chatsData chatsData = new chatsData(chat.getSender(), chat.getSender());

                            chatsDataList.add(chatsData);

                        }

                        adapter = new ChatsAdapter(getActivity(), chatsDataList);
                        rv.setAdapter(adapter);

                    }




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
