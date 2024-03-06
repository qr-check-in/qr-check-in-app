package com.example.qrcheckin;

import android.app.Application;

/**
 * sub application to call certain methods only upon the app being opened.
 */
// https://stackoverflow.com/questions/19415006/how-to-run-method-once-per-app-start , 2013, Adam S
public class OpenApp extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        // Get the app's FcmToken,
        Database db = new Database();

        // Check if an Attendee object associated with this app installation exists
        db.getFcmToken();

    }
}
