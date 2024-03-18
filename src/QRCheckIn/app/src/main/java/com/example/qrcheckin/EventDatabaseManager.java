package com.example.qrcheckin;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDatabaseManager {
    private final FirebaseFirestore db;
    private final CollectionReference eventRef;

    public EventDatabaseManager(){
        this.db = FirebaseFirestore.getInstance();
        this.eventRef = db.collection("events");
    }

    /**
     * Stores an Event object to the Events collection
     * @param event the Event to be stored
     */
    public void storeEvent(Event event){
        eventRef.document().set(event);
        Log.d("Firestore", String.format("Event(%s) stored", event.getEventName()));
    }

    public CollectionReference getEventsRef(){
        return this.eventRef;
    }
}
