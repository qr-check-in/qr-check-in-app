package com.example.qrcheckin;

import android.content.Intent;
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
            // TODO: Process data
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

        // Use the MyNotificationManager to send the notification
        MyNotificationManager.getInstance(this).sendNotification(
                new Random().nextInt(), // Generate a random ID for the notification to ensure they are unique
                getString(R.string.default_notification_channel_id), // Use default notification channel ID
                title,
                messageBody,
                intent);
    }
}
