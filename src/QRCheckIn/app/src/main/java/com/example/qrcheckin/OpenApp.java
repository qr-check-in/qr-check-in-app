package com.example.qrcheckin;

import android.app.Application;

/**
 * sub application to call certain methods only upon the app being opened.
 */
// https://stackoverflow.com/questions/19415006/how-to-run-method-once-per-app-start , 2013, wtsang02
public class OpenApp extends Application{
    public boolean hasCheckedFcmToken;
    @Override
    public void onCreate(){
        super.onCreate();
        hasCheckedFcmToken = false;
    }
}
