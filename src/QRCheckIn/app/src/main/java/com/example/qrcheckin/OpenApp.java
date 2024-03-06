package com.example.qrcheckin;

import android.app.Application;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.atomic.AtomicReference;

/**
 * sub application to call certain methods only upon the app being opened.
 */
// https://stackoverflow.com/questions/19415006/how-to-run-method-once-per-app-start , 2013, Adam S
public class OpenApp extends Application{
    private final AtomicReference<String> reference = new AtomicReference<>();
    @Override
    public void onCreate(){
        super.onCreate();
        // Get the app's FcmToken,
        Database db = new Database();
        getFcmToken();
        Log.d("Firestore", String.format("IN OPENAPP token is (%s) ", reference.get()));
        // Check if an Attendee object associated with this app installation exists
        db.checkExistingAttendees(reference.get());

    }
    /**
     * Retrieves and logs the Firebase Cloud Messaging (FCM) token for this app's installation
     */
    public void getFcmToken() {
        // TEMPORARY
        reference.set("token-789");
        //

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(Utils.TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get and log the new FCM registration token
                    String token = task.getResult();
                    reference.set(token);
                    Log.d(Utils.TAG, token);
                });
    }
}
