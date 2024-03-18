package com.example.qrcheckin;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDatabaseManager {
    private final FirebaseFirestore db;
    private final CollectionReference eventRef;

    /**
     * Constructs EventDatabaseMangaer object. Gets instance of firestore and creates a collection for the 'events' collection
     */
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

    /**
     * Returns a CollectionReference for the 'events' collection
     * @return CollectionReference for the 'events' collection
     */
    public CollectionReference getEventRef(){
        return this.eventRef;
    }
    
    public DocumentReference getEventDoc(String documentId){
        return eventRef.document(documentId);
    }
}
