package com.example.qrcheckin.Attendee;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.qrcheckin.Common.DatabaseManager;
import com.example.qrcheckin.Common.ImageStorageManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;

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
        attendee.getProfile().generateProfilePicture(null);
        attendee.getProfile().getProfilePicture().setGenerated(true);
        // The docID of the attendee object is the associated user's fcmToken string
        getCollectionRef().document(getDocumentID()).set(attendee);
        Log.d("Firestore", String.format("Attendee for token (%s) stored", getDocumentID()));
    }

    /**
     * Updates a boolean field in the Attendee's firebase document
     * @param field String name of the field to be updated
     * @param value Boolean value the field is to be set to
     */
    public void updateAttendeeBoolean(String field, Boolean value){
        getDocRef().update(field, value).addOnSuccessListener(new OnSuccessListener<Void>() {
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
     * Retrieves the Attendee's profile and calls method to generate a new ProfilePicture of their initials
     * @param imageView ImageView for cases where a profile picture has been removed, we need to re-call displayImage() once
     *                  the new profile picture has been successfully uploaded. Null in other cases.
     */
    public void callGenerateProfilePicture(ImageView imageView){
        getDocRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Attendee attendee = documentSnapshot.toObject(Attendee.class);
                if (attendee == null) {
                    Log.e("Firestore", "Attendee doc not found");
                } else {
                    ProfilePicture pfp = attendee.getProfile().getProfilePicture();
                    if (pfp != null && pfp.getGenerated()){
                        ImageStorageManager storage = new ImageStorageManager(pfp, "/ProfilePictures");
                        // Remove profile picture from storage
                        storage.deleteImage();
                        // Generate a new profile picture and update the Attendee's document
                        attendee.getProfile().generateProfilePicture(imageView);
                        updateProfilePicture(Uri.parse(attendee.getProfile().getProfilePicture().getUriString()));
                    }
                }
            }
        });
    }

    /**
     * Deletes the Attendee's ProfilePicture in firestorage and replaces it with an uploaded image Uri
     * @param uri Uri of the new profile picture uploaded by a user
     */
    public void deleteProfilePicture(Uri uri){
        getDocRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Attendee attendee = documentSnapshot.toObject(Attendee.class);
                if (attendee == null) {
                    Log.e("Firestore", "Attendee doc not found");
                } else {
                    ProfilePicture pfp = attendee.getProfile().getProfilePicture();
                    if (pfp != null) {
                        // Remove previous profile picture from storage
                        ImageStorageManager storage = new ImageStorageManager(pfp, "/ProfilePictures");
                        storage.deleteImage();
                        // Update Attendee doc with new picture
                        updateProfilePicture(uri);
                        updateAttendeeBoolean("profile.profilePicture.generated", false);
                    }
                }
            }
        });
    }


    /**
     * Updates a string field in an Attendee's Profile in firestore
     * @param field String of the field to be updated in Profile
     * @param value Stråing of the new value the field is set to
     * @param imageView ImageView for cases where a profile name has been updated, we need to re-call displayImage() once
     *                  the new profile picture has been successfully uploaded. Null in other cases.
     */
    public void updateProfileString(String field, String value, ImageView imageView){
        getDocRef().update("profile."+field, value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "docsnapshot string updated");
                if(field.equals("name")){
                    // For cases where the user updates their name, and thus their generated profile picture must get updated as well
                    callGenerateProfilePicture(imageView);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firestore", "error updating doc",e);
            }
        });
    }

    /**
     * Updates a string field in an Attendee's location in firestore
     * @param field String of the field to be updated in Profile
     * @param value GeoPoint of the new value the field is set to
     */
    public void updateAttendeeLocation(String field, GeoPoint value){
        getDocRef().update(field, value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "docsnapshot geoPoint updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firestore", "error updating doc",e);
            }
        });
    }

    /**
     * Increments the check-in count for an attendee in a map field.
     * @param fieldName The name of the map field ("attendedEvents")
     * @param eventId The ID of the event to increment the check-in count for.
     */
    public void incrementCheckInCount(String fieldName, String eventId) {
        // Use FieldValue.increment to atomically increment the check-in count
        docRef.update(fieldName + "." + eventId, FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "docsnapshot checkIns updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "error updating doc",e);
                    }
                });
    }


}
