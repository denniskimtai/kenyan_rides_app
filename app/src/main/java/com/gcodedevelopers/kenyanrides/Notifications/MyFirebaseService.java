package com.gcodedevelopers.kenyanrides.Notifications;

import com.gcodedevelopers.kenyanrides.ChatsActivity;
import com.gcodedevelopers.kenyanrides.SharedPrefManager;
import com.gcodedevelopers.kenyanrides.user;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()){
            updateToken(refreshToken);
        }

    }

    private void updateToken(String refreshToken) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages");
        Token token = new Token(refreshToken);
        //getting the current user
        user user2 = SharedPrefManager.getInstance(this).getUser();

        reference.child(user2.getMobile_number()).setValue(token);

    }
}
