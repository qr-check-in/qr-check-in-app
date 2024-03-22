package com.example.qrcheckin;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Controls storing and retrieving data from the Attendees firebase collection
 */
public class AttendeeDatabaseManager extends DatabaseManager {

    /**
     * Constructs an AttendeeDatabaseManager for cases where we want to store/retrieve data from Firebase for a specific Attendee
     * @param token String fcmToken of the Attendee whose data we are accessing
     */
    public AttendeeDatabaseManager(String token){
        super("Attendees", token);
    }

    /**
     * Constructs an AttendeeDatabaseManager for cases where we want the Attendee collection (to execute a query for example)
     */
    public AttendeeDatabaseManager(){
        super("Attendees");
    }

    /**
     * Searches Attendee collection for an Attendee whose docID matches fcmToken
     * Does nothing if attendee document is found
     * Calls storeAttendee to create a new attendee object if attendee document does not exist
     *
     */
    public void checkExistingAttendees(){
        getDocRef().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()){
                        // no such document exists
                        storeAttendee();
                    }
                    else{
                        Log.d("Firestore", "attendee already exists");
                    }
                }
            }
        });
    }

    /**
     * Creates a new Attendee object and stores it to the Attendee collection
     */
    public void storeAttendee(){
        Attendee attendee = new Attendee();
        // The docID of the attendee object is the associated user's fcmToken string
        getCollectionRef().document(getDocumentID()).set(attendee);
        Log.d("Firestore", String.format("Attendee for token (%s) stored", getDocumentID()));
    }

    /**
     * Updates the trackGeolocation field of the Attendee with the docID fcmToken
     * @param isShared Boolean value trackGeolocation is set to
     */
    public void updateAttendeeGeolocation(Boolean isShared){
        getDocRef().update("profile.trackGeolocation", isShared).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "docsnapshot boolean updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firestore", "error updating doc",e);
            }
        });
    }

    /**
     * Updates the profilePicture field in an Attendee's Profile
     * @param uri Uri of the ProfilePicture
     */
    public void updateProfilePicture(Uri uri){
        // Check if uri is null, in which case we are just removing the profile picture
        String uriString = null;
        if(uri != null){
            uriString= uri.toString();
        }
        // Update the uriString field
        getDocRef().update("profile.profilePicture.uriString", uriString).addOnSuccessListener(new OnSuccessListener<Void>() {
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
     * Updates a string field in an Attendee's Profile in firestore
     * @param field String of the field to be updated in Profile
     * @param value String of the new value the field is set to
     */
    public void updateProfileString(String field, String value){
        getDocRef().update("profile."+field, value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "docsnapshot string updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firestore", "error updating doc",e);
            }
        });
    }

}
