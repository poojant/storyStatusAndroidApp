package com.example.poojan.whatsappstatus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /*public void sendNotification(String message)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_person_white_24dp)
                        .setContentTitle("FCM Message")
                        //.setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setContentText(message+ " has uploaded his status")
                        //.setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());


    }*/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
    PendingIntent.FLAG_ONE_SHOT);

    NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_person_white_24dp)
                    .setContentTitle("FCM Message")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    //.setContentText(message+ " has uploaded his status")
                    //.setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());

}
}
