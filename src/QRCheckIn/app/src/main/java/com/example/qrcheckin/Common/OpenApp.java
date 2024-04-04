package com.example.qrcheckin.Common;

import android.app.Application;

/**
 * sub application to call certain methods only upon the app being opened.
 */
// https://stackoverflow.com/questions/19415006/how-to-run-method-once-per-app-start , 2013, wtsang02
public class OpenApp extends Application{
    /**
     * Flag indicating whether the FCM token check has been performed.
     */
    public boolean hasCheckedFcmToken;
    /**
     * Initializes the application and sets hasCheckedFcmToken flag to false.
     */
    @Override
    public void onCreate(){
        super.onCreate();
        hasCheckedFcmToken = false;
    }
}



