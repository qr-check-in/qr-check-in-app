package com.example.qrcheckin;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.UUID;

/**
 * A class that controls storing data to the firestore database collections
 */
public class Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private final CollectionReference eventsRef = db.collection("events");
    private final CollectionReference attendeesRef = db.collection("Attendees");


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

    /**
     * Searches Attendee collection for an Attendee whose docID matches fcmToken
     * Calls storeAttendee to create a new attendee object if none exist
     * @param fcmToken the fcmToken of the Attendee we're searching for
     */
    public void checkExistingAttendees(String fcmToken){
        DocumentReference docRef = attendeesRef.document(fcmToken);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Attendee attendee = documentSnapshot.toObject(Attendee.class);
                if(attendee == null){
                    // No such Attendee already exists
                    storeAttendee(fcmToken);
                }
                else{
                    Log.d("Firestore", "attendee already exists");
                }
            }
        });


    }

    /**
     * Creates a new Attendee object and stores it to the Attendee collection
     * @param fcmToken the fcmToken of the user we're creating an Attendee object for
     */
    public void storeAttendee(String fcmToken){
        Attendee attendee = new Attendee();
        attendeesRef.document(fcmToken).set(attendee);
        Log.d("Firestore", String.format("Attendee for token (%s) stored", fcmToken));
    }

    /**
     * Retrieves and logs the Firebase Cloud Messaging (FCM) token for this app's installation
     */
    public void getFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(Utils.TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get and log the new FCM registration token
                    String token = task.getResult();
                    Log.d(Utils.TAG, token);
                    checkExistingAttendees(token);
                });
    }
}
