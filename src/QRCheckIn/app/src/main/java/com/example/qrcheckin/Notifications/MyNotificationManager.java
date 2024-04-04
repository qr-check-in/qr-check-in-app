package com.example.qrcheckin.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.example.qrcheckin.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

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
            mInstance = new MyNotificationManager(context.getApplicationContext());  // getApplicationContext() prevents memory leaks, ignore the warning
        }
        return mInstance;
    }

    /**
     * Build a notification with the mentioned title and body, then send it in the specified channel.
     *      - Creating a notification with an identical ID to another will cause it to be
     *          overwritten/updated by the new one. You can generate a random one with {@link java.util.Random}
     *      - Use channel IDs imported from strings.xml or some other resource file to ensure they
     *          actually initialized
     *      - See {@link Intent#addFlags(int)} for intent creation, use the flag
     *          "FLAG_ACTIVITY_CLEAR_TOP" by default
     *
     * @param id        An ID associated with this specific notification which can be used to track it
     * @param channelId The channel ID you want to send the notification to.
     * @param title     The string title of the notification
     * @param body      The string body text of the notification
     * @param intent    The activity you intend the user to open when they click the notification.
     */
    public void sendNotification(int id, String channelId, String title, String body, Intent intent) {

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

    /**
     * Sends notification to Topic
     * @param topic
     * @param title
     * @param body
     */
    public void sendNotificationToTopic(String topic, String title, String body) {
        // Create a RemoteMessage object with the necessary data
        RemoteMessage.Builder messageBuilder = new RemoteMessage.Builder(topic);
        messageBuilder
                .setMessageId(Integer.toString(new Random().nextInt())) // Generate a random message ID
                .addData("title", title)
                .addData("body", body);
        RemoteMessage remoteMessage = messageBuilder.build();

        // Send the message
        FirebaseMessaging.getInstance().send(remoteMessage);
    }
}