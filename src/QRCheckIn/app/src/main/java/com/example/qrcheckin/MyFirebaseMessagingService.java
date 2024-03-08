package com.example.qrcheckin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import java.util.Random;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    /**
     * Handles when a new token is created. This occurs upon:
     *     1. Initial app startup
     *     2. User uninstalls/reinstalls app
     *     3. User clears app data
     *     4. App is restored to a new device
     * @param token The token used for sending messages to this application instance. This token is
     *     the same as the one retrieved by {@link FirebaseMessaging#getToken()}.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(Utils.TAG, "Refreshed token: " + token);
        // TODO: create a "sendRegistrationToServer(token)" function which sends the new token to the backend
        // sendRegistrationToServer(token);

    }

    /**
     * Called when a new message is received. Handles checking data payload and calls a function to
     *      send the message.
     * @param message Remote message that has been received.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        // Check if the message contains data
        if (message.getData().size() > 0) {
            //TODO: Process data
            Log.d(Utils.TAG, "Message data payload: " + message.getData());
        }

        // Check if the message contains a notification payload
        if (message.getNotification() != null) {
            Log.d(Utils.TAG, "Message Notification Body: " + message.getNotification().getBody());
        }

        // Extract message text and title, set to default if there is nothing
        String title = "New Message";
        String messageBody = "You've got a new message.";
        if (message.getNotification() != null) {
            title = message.getNotification().getTitle() == null ? title : message.getNotification().getTitle();
            messageBody = message.getNotification().getBody() == null ? messageBody : message.getNotification().getBody();
        }

        // Create an intent that will open the main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Create the "Event Updates" channel (if necessary)
        String channelId = getString(R.string.notification_channel_event_updates_id);
        String channelName = getString(R.string.notification_channel_event_updates_name);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        createNotificationChannel(channelId, channelName, importance);

        // Send the notification
        MyNotificationManager.getInstance(this).sendNotification(
                new Random().nextInt(), // Generate a random ID for the notification to ensure they are unique
                channelId,
                title,
                messageBody,
                intent);
    }

    /**
     * Creates a notification channel with the specified fields.
     *
     * @param channelId             The channel ID you want to send the notification to.
     * @param channelName           The channel name matching the ID
     * @param channelImportance     The integer importance of the channel [0-4]
     */
    public void createNotificationChannel(String channelId, String channelName, int channelImportance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, channelImportance);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            // Create manager
            NotificationManager manager = this.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        } else {
            Log.w(Utils.TAG, "API 26+ required to use NotificationChannel class");
        }
    }
}
