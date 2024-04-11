package com.medical.my_medicos.activities.notifications.receivers;

import static androidx.media3.common.util.NotificationUtil.IMPORTANCE_HIGH;
import static androidx.media3.common.util.NotificationUtil.createNotificationChannel;

import android.app.Notification;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.job.JobDetailsActivity;
import com.medical.my_medicos.activities.job.JobsActivity;

import java.util.Random;

public class FirebaseServiceReceivernotification extends FirebaseMessagingService {

    private final String CHANNEL_ID = "Job Alerts";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String generatedDocId = remoteMessage.getData().get("generatedDocId");

        Intent intent = new Intent(this, JobDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("documentid", generatedDocId);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.placeholderjobouter, null);
        Bitmap largeIcon = getBitmapFromDrawable(drawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(manager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Extract data from the message
        String title = remoteMessage.getData().get("title");
        String organiser = remoteMessage.getData().get("Organiser");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("New Opening for " + title)
                .setContentText("Posted by " + organiser)
                .setSmallIcon(R.drawable.logoooooooofor)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        manager.notify(notificationId, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager manager) {
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.new_job);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Job Alerts", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Get daily new opening of jobs in hospitality near you");
        channel.enableLights(true);
        channel.setLightColor(Color.WHITE);
        channel.setSound(soundUri, attributes); // Set the custom sound for the channel

        manager.createNotificationChannel(channel);
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return bitmap;
    }
}
