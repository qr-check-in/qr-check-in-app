package com.example.qrcheckin;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Controls storing and retrieving data from the events firebase collection
 */
public class EventDatabaseManager {
    private final FirebaseFirestore db;
    private final CollectionReference eventCollectionRef;
    private String eventDocID;
    private DocumentReference docRef;

    /**
     * Constructs EventDatabaseManager object for cases where we want to get the data of one specific Event already in firebase
     */
    public EventDatabaseManager(String docID){
        this.db = FirebaseFirestore.getInstance();
        this.eventCollectionRef = db.collection("events");
        this.eventDocID = docID;
        this.docRef = eventCollectionRef.document(docID);
    }

    /**
     * Constructs EventDatabaseManager for cases where we don't have a docID, meaning an event has yet to be stored in firebase
     * Or for cases where we want to get the events collection (to execute a query for example)
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

    /**
     * Returns a DocumentReference for an Event document
     * @return DocumentReference of the Event
     */
    public DocumentReference getEventDocRef(){
        return this.docRef;
    }

    /**
     * Adds an Attendee's firebase doc ID string to the Event's list of attending/signed-up attendee IDs
     * @param fieldName String of the list to be updated (attendee or signups)
     * @param attendeeID String ID of the attendee
     */
    public void addAttendee(String fieldName, String attendeeID){
        docRef.update(fieldName, FieldValue.arrayUnion(attendeeID));
    }

    /**
     * Removes an Attendee's firebase doc ID string from the Event's list of attending/signed-up attendee IDs
     * @param fieldName String of the list to be updated (attendee or signups)
     * @param attendeeID String ID of the attendee
     */
    public void removeAttendee(String fieldName, String attendeeID){
        docRef.update(fieldName, FieldValue.arrayRemove(attendeeID));
    }
}
