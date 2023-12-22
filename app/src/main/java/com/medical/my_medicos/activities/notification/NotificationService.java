package com.medical.my_medicos.activities.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NotificationService extends FirebaseMessagingService {
    NotificationManager notificationManager;
    Notification notification;
    Uri defaultSoundUri;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String text = data.get("text");

            // Display notification
            showNotification(title, text);
        }

        // Check if the message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            // Handle notification payload (you can customize this part)
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Display notification
            showNotification(title, body);
        }
    }

    private void showNotification(String title, String text) {
        // Create an intent to open your main activity when the notification is clicked
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Create the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "your_channel_id")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Get the notification manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the device is running Android Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a notification channel
            NotificationChannel channel = new NotificationChannel("your_channel_id", "Your Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Display the notification
        notificationManager.notify(0, notificationBuilder.build());
    }
}