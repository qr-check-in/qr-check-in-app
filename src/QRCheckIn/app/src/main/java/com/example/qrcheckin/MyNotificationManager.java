package com.example.qrcheckin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
public class MyNotificationManager {

    private final Context mContext;
    private static MyNotificationManager mInstance;
    private final NotificationManager mManager;

    private MyNotificationManager(Context context) {
        mContext = context;
        mManager = context.getSystemService(NotificationManager.class);
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void createNotificationChannel(String channelId, String channelName, String channelDescription) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            mManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(int id, String channelId, String title, String body, Intent intent) {

        // Create channel
        createNotificationChannel(channelId,
                mContext.getString(R.string.notification_channel_event_updates_name),
                mContext.getString(R.string.notification_channel_event_updates_description));

        // Build notification
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: Replace this with the notification image
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        mManager.notify(id, builder.build());
    }
}