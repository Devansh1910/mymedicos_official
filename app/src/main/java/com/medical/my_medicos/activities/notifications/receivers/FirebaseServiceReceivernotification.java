package com.medical.my_medicos.activities.notifications.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.job.JobDetailsActivity;

import java.util.Random;

public class FirebaseServiceReceivernotification extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "Job Alerts";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Initialize NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification ID to allow for future updates
        int notificationId = new Random().nextInt();

        // Create a notification channel if necessary (Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }

        // Get title and organizer from the remote message
        String title = remoteMessage.getData().get("title");
        String organiser = remoteMessage.getData().get("Organiser");

        // Create an intent that will fire when the notification is tapped
        Intent intent = new Intent(this, JobDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Get the large icon for the notification
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.placeholderjobouter, null);
        Bitmap largeIcon = getBitmapFromDrawable(drawable);

        // Build the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("New Opening: " + title)
                .setContentText("Organized by " + organiser)
                .setSmallIcon(R.drawable.logoooooooofor)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Notify
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager manager) {
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.new_job);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Job Alerts", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Notifications for job openings");
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);
        channel.setSound(soundUri, audioAttributes);
        manager.createNotificationChannel(channel);
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
        return null;
    }
}
