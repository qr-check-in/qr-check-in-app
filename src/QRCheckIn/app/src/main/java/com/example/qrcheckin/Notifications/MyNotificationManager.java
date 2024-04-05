package com.example.qrcheckin.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.qrcheckin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class MyNotificationManager {

    private final Context mContext;
    private static MyNotificationManager mInstance;
    private final NotificationManager mManager;

    public MyNotificationManager(Context context) {
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
                .setSmallIcon(R.drawable.app_icon_resize_24)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        mManager.notify(id, builder.build());
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
                .addHeader("Authorization", "key=" + mContext.getString(R.string.server_key))
                .build();
        okhttp3.Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}