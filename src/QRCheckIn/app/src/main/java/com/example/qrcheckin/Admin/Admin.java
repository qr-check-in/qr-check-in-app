package com.example.qrcheckin.Admin;

import android.util.Log;

import com.example.qrcheckin.Common.Image;
import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.Event.Event;
import com.example.qrcheckin.Attendee.Attendee;
import com.example.qrcheckin.Event.EventDatabaseManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.qrcheckin.Attendee.Profile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Admin class responsible for administrative actions such as viewing and deleting events, profiles, and images.
 */

public class Admin extends Attendee {
    private final FirebaseFirestore db;
    private EventDatabaseManager eventDb;

    public interface EventsCallback {
        void onEventsFetched(List<Event> events);
    }
    public interface ImageFetchCallback {
        void onImagesFetched(List<String> imageUris);
    }

    public interface ProfileCallback {
        void onProfileFetched(Map<String, Object> profile, String profilePic);
        void onError(Exception e);
    }

    /**
     * Constructor for admin initializes firestore instance.
     */
    public Admin() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Retrieves & prints details of an event.
     *
     * @param eventId the ID of the event to view.
     */
    public void viewEvent(String eventId) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        System.out.println("Event data: " + documentSnapshot.getData());
                    } else {
                        System.out.println("No such event exists!");
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching event: " + e));
    }

    /**
     * Deletes an event from the database.
     * US 04.01.01
     *
     * @param eventId the ID of the event to delete.
     */
    public void deleteEvent(String eventId) {
        db.collection("events").document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Convert the documentSnapshot to an Event object
                    Event event = documentSnapshot.toObject(Event.class);
                    Image poster = event.getPoster();
                    if (event != null && poster!=null) {
                        // Use your Event object here
                        ImageStorageManager storage = new ImageStorageManager(poster, "/EventPosters");
                        storage.deleteImage();
                    }
                } else {
                    Log.d("Firestore", "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@android.support.annotation.NonNull Exception e) {
                Log.d("Firestore", "Failed to fetch document: ", e);
            }
        });
        db.collection("events").document(eventId).delete()
                .addOnSuccessListener(aVoid -> System.out.println("Event successfully deleted!"))
                .addOnFailureListener(e -> System.out.println("Error deleting event: " + e));   }

    /**
     * Retrieves and prints the details of a user profile.
     *
     * @param userId the ID of the user profile to view.
     */
    public void viewProfile(String userId, ProfileCallback callback) {
        db.collection("Attendees").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Access the nested profile field within the Attendees document
                        Map<String, Object> profile = (Map<String, Object>) documentSnapshot.get("profile");
                        String profilePicUrl = (String) documentSnapshot.get("profile.profilePicture.uriString");
                        Log.d("profilePicAdmin", "profile uri:"+profilePicUrl);
                        if (profile != null) {
                            callback.onProfileFetched(profile, profilePicUrl);
                        } else {
                            callback.onError(new Exception("No profile data found!"));
                        }
                    } else {
                        callback.onError(new Exception("No such profile exists!"));
                    }
                })
                .addOnFailureListener(callback::onError);
    }


    /**
     * Deletes a user profile from the database.
     * US 04.02.01
     *
     * @param userId the ID of the user profile to delete.
     */
    public void deleteProfile(String userId) {
        DocumentReference userDocRef = db.collection("Attendees").document(userId);

        // Using null to clear the profile field or FieldValue.delete() to remove the field
        userDocRef.update("profile", FieldValue.delete())
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Profile successfully deleted!");
                    // Additional logic after successful deletion, such as updating the UI, can be placed here.
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error deleting profile: " + e);
                    // Handle the error scenario, such as notifying the user or logging the error.
                });
    }

    /**
     * Retrieves and prints the details of an image, which could be a profile picture or event poster.
     *
     * @param documentId The ID of the document (either an Attendee or an Event).
     * @param imageType  The type of image to view ("profilePicture" or "poster").
     */
    public void viewImage(String documentId, String imageType) {
        String collectionPath;
        String fieldPath;
        // Determine the collection and field path based on the image type
        if ("profilePicture".equals(imageType)) {
            collectionPath = "Attendees";
            fieldPath = "profile.profilePicture.uriString";
        } else if ("poster".equals(imageType)) {
            collectionPath = "events";
            fieldPath = "poster.uriString";
        } else {
            System.out.println("Invalid image type specified.");
            return;
        }
        // Get the document reference from the appropriate collection
        DocumentReference docRef = db.collection(collectionPath).document(documentId);
        // Fetch the document and extract the image URI string
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String uriString = documentSnapshot.getString(fieldPath);
                        if (uriString != null && !uriString.isEmpty()) {
                            System.out.println(imageType + " data: " + uriString);
                        } else {
                            System.out.println("No " + imageType + " data found.");
                        }
                    } else {
                        System.out.println("No such document exists!");
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching image: " + e));
    }

    /**
     * Deletes an image from the database.
     * US 04.03.01
     *
     * @param documentId The ID of the document (either an Attendee or an Event).
     * @param imageType  The type of image to delete ("profilePicture" or "poster").
     */
    public void deleteImage(String documentId, String imageType) {
        String collectionPath;
        String fieldPath;
        // Determine the collection and field path based on the image type
        if ("profilePicture".equals(imageType)) {
            collectionPath = "Attendees";
            fieldPath = "profile.profilePicture";
        } else if ("poster".equals(imageType)) {
            collectionPath = "events";
            fieldPath = "poster";
        } else {
            System.out.println("Invalid image type specified.");
            return;
        }
        // Get the document reference from the appropriate collection
        DocumentReference docRef = db.collection(collectionPath).document(documentId);
        // Update the document to remove the image field
        docRef.update(fieldPath, FieldValue.delete())
                .addOnSuccessListener(aVoid -> System.out.println(imageType + " successfully deleted."))
                .addOnFailureListener(e -> System.out.println("Error deleting " + imageType + ": " + e));
    }

    /**
     * Retrieves & prints the list of all events.
     * US 04.04.01
     */
    public void browseEvents(EventsCallback callback) {
        String id = db.collection("events").getId();
        db.collection("events").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Event> events = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Event event = documentSnapshot.toObject(Event.class);
                        events.add(event);
                    }
                    callback.onEventsFetched(events);
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error fetching events: " + e);
                    callback.onEventsFetched(new ArrayList<>()); // In case of failure, return an empty list
                });
    }

    /**
     * Retrieves & prints the list of all profiles.
     * US 04.05.01.
     */
    public void browseProfiles(final ProfileCallback callback) {
        db.collection("Attendees").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Profile> profilesList = new ArrayList<>();
                    List<String> documentIds = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Map<String, Object> profileMap = (Map<String, Object>) documentSnapshot.get("profile");
                        if (profileMap != null) {
                            String name = (String) profileMap.get("name");
                            if (name != null) {
                                Profile profile = new Profile();
                                profile.setName(name);
                                profilesList.add(profile);
                                documentIds.add(documentSnapshot.getId());
                                Log.d("AdminViewProfiles", "Fetched profile - Name: " + name);
                            } else {
                                Log.d("AdminViewProfiles", "Name field is missing in the profile document with ID: " + documentSnapshot.getId());
                            }
                        } else {
                            Log.d("AdminViewProfiles", "Profile map is missing in the document with ID: " + documentSnapshot.getId());
                        }
                    }
//                    if (callback != null) {
//                        callback.onProfilesFetched(profilesList, documentIds);
//                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Admin", "Error fetching profiles", e);
//                    if (callback != null) {
//                        callback.onProfilesFetched(new ArrayList<>(), documentIds); // Callback with an empty list in case of failure
//                    }
                });
    }

    /**
     * Retrieves & prints the list of all images.
     * US 04.06.01
     */
    public void browseImages(final ImageFetchCallback callback) {
        List<String> imageUris = new ArrayList<>();

        // Browse profile pictures
        db.collection("Attendees").get()
                .addOnSuccessListener(attendeesSnapshots -> {
                    for (DocumentSnapshot attendeeSnapshot : attendeesSnapshots) {
                        if (attendeeSnapshot.contains("profile.profilePicture")) {
                            String profilePicUri = attendeeSnapshot.getString("profile.profilePicture.uriString");
                            if (profilePicUri != null) {
                                imageUris.add(profilePicUri);
                            }
                        }
                    }
                    // Continue to browse event posters after fetching profile pictures
                    db.collection("events").get()
                            .addOnSuccessListener(eventsSnapshots -> {
                                for (DocumentSnapshot eventSnapshot : eventsSnapshots) {
                                    if (eventSnapshot.contains("poster")) {
                                        String posterUri = eventSnapshot.getString("poster.uriString");
                                        if (posterUri != null) {
                                            imageUris.add(posterUri);
                                        }
                                    }
                                }
                                if (callback != null) {
                                    callback.onImagesFetched(imageUris);
                                }
                            })
                            .addOnFailureListener(e -> System.out.println("Error fetching event posters: " + e));
                })
                .addOnFailureListener(e -> System.out.println("Error fetching profile pictures: " + e));
    }



}
