package com.example.qrcheckin;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * A class that controls storing data to the firestore database collections
 */
public class Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
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

    /**
     * Searches Attendee collection for an Attendee whose docID matches fcmToken
     * Does nothing if attendee document is found
     * Calls storeAttendee to create a new attendee object if attendee document does not exist
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
        // The docID of the attendee object is the associated user's fcmToken string
        attendeesRef.document(fcmToken).set(attendee);
        Log.d("Firestore", String.format("Attendee for token (%s) stored", fcmToken));
    }

    /**
     * Updates the trackGeolocation field of the Attendee with the docID fcmToken
     * @param fcmToken String ocID of the attendee to be updated
     * @param isShared Boolean value trackGeolocation is set to
     */
    public void updateAttendeeGeolocation(String fcmToken, Boolean isShared){
        DocumentReference attendeeRef = db.collection("Attendees").document(fcmToken);
        attendeeRef.update("profile.trackGeolocation", isShared).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "docsnapshot updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firestore", "error updating doc",e);
            }
        });
    }

    /**
     * Retrieves and logs the Firebase Cloud Messaging (FCM) token for this app's installation
     * @param editor a SharedPreferences.Editor from the calling activity to save the token string value
     */
    public void getFcmToken(SharedPreferences.Editor editor) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(Utils.TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get and log the new FCM registration token
                    String token = task.getResult();
                    Log.d(Utils.TAG, token);
                    // save token string
                    editor.putString("token", token);
                    editor.apply();
                });
    }
}
