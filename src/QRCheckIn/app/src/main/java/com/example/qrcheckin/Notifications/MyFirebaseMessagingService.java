package com.example.qrcheckin.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qrcheckin.Common.MainActivity;
import com.example.qrcheckin.Event.OrganizersEventPageActivity;
import com.example.qrcheckin.R;
import com.example.qrcheckin.Common.Utils;
import com.example.qrcheckin.Attendee.AttendeeDatabaseManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


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

        // use SharedPreferences to save this app installation's fcmToken
        // https://stackoverflow.com/questions/51834864/how-to-save-a-fcm-token-in-android , 2018, Whats Going On
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.apply();
        // checkExistingAttendees will store a new Attendee document if there's not already an existing doc for this fcmToken
        AttendeeDatabaseManager db = new AttendeeDatabaseManager(token);
        db.checkExistingAttendees();

    }

    /**
     * Retrieves and logs the Firebase Cloud Messaging (FCM) token for this app's installation
     * @param editor a SharedPreferences.Editor from the calling activity to save the token string value
     */
    public void getFcmToken(SharedPreferences.Editor editor) {
        // this method is no longer used now that new attendee docs are stored by onNewToken method
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(Utils.TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get and log the new FCM registration token
                    String token = task.getResult();
                    Log.d(Utils.TAG, token);
                    // save token string
                    editor.putString("token", token);
                    editor.apply();
                });
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

        // Extract message title, body, eventID, set to default if there is nothing
        String title = "New Message";
        String messageBody = "You've got a new message.";
        String eventId = "";
        if (message.getData().containsKey("eventID")) {
            eventId = message.getData().get("eventID");
        }

        if (message.getNotification() != null) {
            title = message.getNotification().getTitle() == null ? title : message.getNotification().getTitle();
            messageBody = message.getNotification().getBody() == null ? messageBody : message.getNotification().getBody();
        }

        Intent intent;
        // if and eventID was passed then open the event page for respective to the id
        if (message.getData().containsKey("eventID") && !TextUtils.isEmpty(eventId)) {
            // Create an intent that will open the OrganizersEventPageActivity
            intent = new Intent(this, OrganizersEventPageActivity.class);
            intent.putExtra("DOCUMENT_ID", eventId); // Pass the eventId to the intent
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else {
            // otherwise open the app and to home page MainActivity
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        // Create the "Event Updates" channel (if necessary)
        String channelId = getString(R.string.notification_channel_event_updates_id);
        String channelName = getString(R.string.notification_channel_event_updates_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;
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
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            // Create manager
            NotificationManager manager = this.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        } else {
            Log.w(Utils.TAG, "API 26+ required to use NotificationChannel class");
        }
    }
}
