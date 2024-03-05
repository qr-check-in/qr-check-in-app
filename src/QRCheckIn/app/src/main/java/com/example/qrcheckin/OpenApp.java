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
    @Override
    public void onCreate(){
        super.onCreate();
        // Get the app's FcmToken, call checkExistingProfiles on it
        Database db = new Database();
        db.checkExistingProfiles(getFcmToken());


    }
    /**
     * Retrieves and logs the Firebase Cloud Messaging (FCM) token for this app's installation
     * @return String of the app's token
     */
    public String getFcmToken() {
        final AtomicReference<String> reference = new AtomicReference<>();
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
        return reference.get();
    }
}
