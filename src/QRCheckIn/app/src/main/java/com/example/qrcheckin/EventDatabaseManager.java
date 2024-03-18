package com.example.qrcheckin;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDatabaseManager {
    private final FirebaseFirestore db;
    private final CollectionReference eventCollectionRef;

    /**
     * Constructs EventDatabaseMangaer object. Gets instance of firestore and creates a collection for the 'events' collection
     */
    public EventDatabaseManager(){
        this.db = FirebaseFirestore.getInstance();
        this.eventCollectionRef = db.collection("events");
    }

    /**
     * Stores an Event object to the Events collection
     * @param event the Event to be stored
     */
    public void storeEvent(Event event){
        eventCollectionRef.document().set(event);
        Log.d("Firestore", String.format("Event(%s) stored", event.getEventName()));
    }

    /**
     * Returns a CollectionReference for the 'events' collection
     * @return CollectionReference for the 'events' collection
     */
    public CollectionReference getEventCollectionRef(){
        return this.eventCollectionRef;
    }
    
    public DocumentReference getEventDocRef(String documentId){
        return eventCollectionRef.document(documentId);
    }
}
