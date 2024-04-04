package com.example.qrcheckin.Event;

import android.util.Log;

import com.example.qrcheckin.Common.DatabaseManager;
import com.example.qrcheckin.ClassObjects.Event;

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
     */
    public void storeEvent(Event event){
        getCollectionRef().document().set(event);
        Log.d("Firestore", String.format("Event(%s) stored", event.getEventName()));
    }
}
