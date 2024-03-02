package com.example.qrcheckin;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

/**
 * A class that controls storing data to the firestore database collections
 */
public class Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private final CollectionReference eventsRef = db.collection("Events");;


    /**
     * Stores an Event object to the Events collection
     * @param event the Event to be stored
     */
    public void storeEvent(Event event){
        eventsRef.document().set(event);
        Log.d("Firestore", String.format("Event(%s) stored", event.getEventName()));
    }

    public void getEvent(UUID eventID){

    }

}
