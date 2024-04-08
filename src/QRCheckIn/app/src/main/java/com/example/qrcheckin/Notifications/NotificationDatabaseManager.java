package com.example.qrcheckin.Notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qrcheckin.Common.DatabaseManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/**
 * Controls storing and retrieving data from the notifications firebase Collection
 */
public class NotificationDatabaseManager extends DatabaseManager {
    /**
     * Constructor NotificationDatabaseManager object for cases where we want to retrieve a Notification already in db
     */
    public NotificationDatabaseManager(String docID){
        super("Notifications", docID);
    }

    /**
     * Constructs NotifcationDatabaseManager for cases where we don't have a docID, meaning an notifcation has yet to be stored in firebase
     * Or for cases where we want to get the notifications collection (to execute a query for example)
     */
    public NotificationDatabaseManager(){
        super("Notifications");
    }


    /**
     * Stores the new notification into the db
     * @param notification
     * @return the docID of the new notification created
     */
    public String storeNotification(Notification notification) {
        DocumentReference docRef = getCollectionRef().document();
        docRef.set(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Notification stored successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error storing notification", e);
                    }
                });

        // Return the document ID of the newly created notification
        return docRef.getId();
    }
}

