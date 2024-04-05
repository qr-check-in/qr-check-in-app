package com.example.qrcheckin.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qrcheckin.Common.MainActivity;
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


    /**
     * Sends notification to a specific topic.
     * @param topic The topic to send the notification to.
     * @param title The title of the notification.
     * @param body The body of the notification.
     */
    public void sendNotificationToTopic(String topic, String title, String body) {
        //https://stackoverflow.com/questions/55948318/how-to-send-a-firebase-message-to-topic-from-android
        RequestQueue mRequestQue = Volley.newRequestQueue(this);

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + topic);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", body);
            //replace notification with data when went send data
//            json.put("notification", notificationObj);

            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    response -> Log.d("MUR", "onResponse: "),
                    error -> Log.d("MUR", "onError: " + error.networkResponse)
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");

                    // https://www.youtube.com/watch?v=gCQOlkw8YTA, 2024, how to get the server key
                    header.put("authorization", "AAAAJ-wSUZg:APA91bESByqbcdYeTLPXw4NYXsW8KhT8SkNfLmYw839LdvZSbdn_04pMZkwX7oIH4WwxTHI8KAMUyLJbQcy6T1Px75mZ4KJU__J-vIdF01_ExJZzdmVCIRB7GhMQ-tgojToKEYzOD72x");
                    return header;
                }
            };

            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    OkHttpClient mClient = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void sendMessageToTopic(final JSONArray recipients, final String title, final String body, final String eventID) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);

                    JSONObject data = new JSONObject();
                    data.put("eventID", eventID);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);
//                    root.put("to", "/topics/" + topic);

                    String result = postToFCM(root.toString());
                    Log.d("MyFirebaseMessagingSystem", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    Log.d("MyFirebaseMessagingSystem", "Message sent successfully to topic:: " + body);
                } else {
                    Log.d("MyFirebaseMessagingSystem", "Failed to send message to topic: " + body);

                }
            }
        }.execute();
    }

    public String postToFCM(String bodyString) throws IOException {
        RequestBody body = RequestBody.create(JSON, bodyString);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + getString(R.string.server_key))
                .build();
        okhttp3.Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}
