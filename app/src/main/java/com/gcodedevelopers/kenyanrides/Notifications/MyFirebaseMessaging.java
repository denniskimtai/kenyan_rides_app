package com.gcodedevelopers.kenyanrides.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.gcodedevelopers.kenyanrides.ChatsActivity;
import com.gcodedevelopers.kenyanrides.SharedPrefManager;
import com.gcodedevelopers.kenyanrides.user;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sented");

        //getting the current user
        user user2 = SharedPrefManager.getInstance(this).getUser();

        if (sented.equals(user2.getMobile_number())){
            sendNotification(remoteMessage);
        }


    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user1 = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user1.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", user1);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j>0){
            i = j;
        }

        noti.notify(i, builder.build());


    }
}
