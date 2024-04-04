package com.medical.my_medicos.activities.notifications;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MedicosFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages.
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Send this token to your server or store it on your user database
    }
}
