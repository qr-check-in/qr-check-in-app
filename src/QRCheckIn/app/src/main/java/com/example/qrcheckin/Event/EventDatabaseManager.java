package com.example.qrcheckin.Event;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qrcheckin.Common.DatabaseManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/**
 * Controls storing and retrieving data from the events firebase collection
 */
public class EventDatabaseManager extends DatabaseManager {
    /**
     * Constructs EventDatabaseManager object for cases where we want to get the data of one specific Event already in firebase
     */
    public EventDatabaseManager(String docID){
        super("events", docID);
    }

    /**
     * Constructs EventDatabaseManager for cases where we don't have a docID, meaning an event has yet to be stored in firebase
     * Or for cases where we want to get the events collection (to execute a query for example)
     */
    public EventDatabaseManager(){
        super("events");
    }

    /**
     * Stores an Event object to the Events collection
     * @param event the Event to be stored
     * @return the docID of the new event created
     */
    public String storeEvent(Event event){
        DocumentReference docRef = getCollectionRef().document();
        docRef.set(event)
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
