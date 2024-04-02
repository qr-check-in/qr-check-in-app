package com.example.qrcheckin;

import android.util.Log;

/**
 * Controls storing and retrieving data from the notifications firebase Collection
 */
public class NotificationDatabaseManager extends DatabaseManager{
    /**
     * Constructor NotificationDatabaseManager object for cases where we want to retrieve a Notification already in db
     */
    public NotificationDatabaseManager(String docID){
        super("Notifications", docID);
    }

    /**
     * Stores a Notification object to the Notification collection
     * @param notification the notification to be stored
     */
    public void storeNotification(Notification notification){
        getCollectionRef().document().set(notification);
        Log.d("Firestore", String.format("Notification(%s) stored", notification.getTitle()));
    }
}
