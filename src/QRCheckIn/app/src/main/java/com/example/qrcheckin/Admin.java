package com.example.qrcheckin;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Admin class responsible for administrative actions such as viewing and deleting events, profiles, and images.
 */

public class Admin extends Attendee{
    private final FirebaseFirestore db;
    public interface ProfilesCallback {
        void onProfilesFetched(List<Map<String, Object>> profiles);
    }
    public interface EventsCallback {
        void onEventsFetched(List<Event> events);
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
        db.collection("events").document(eventId).delete()
                .addOnSuccessListener(aVoid -> System.out.println("Event successfully deleted!"))
                .addOnFailureListener(e -> System.out.println("Error deleting event: " + e));
    }
 
    /**
     * Retrieves and prints the details of a user profile.
     *
     * @param userId the ID of the user profile to view.
     */
    public void viewProfile(String userId) {
        db.collection("Attendees").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Access the nested profile field within the Attendees document
                        Map<String, Object> profile = (Map<String, Object>) documentSnapshot.get("profile");
                        if (profile != null) { // Check if the profile field exists
                            System.out.println("Profile data: " + profile);
                        } else {
                            System.out.println("No profile data found!");
                        }
                    } else {
                        System.out.println("No such profile exists!");
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching profile: " + e));
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
    public void browseProfiles(ProfilesCallback callback) {
        db.collection("Attendees").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> profiles = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Map<String, Object> profile = (Map<String, Object>) documentSnapshot.get("profile");
                        if (profile != null) {
                            profiles.add(profile);
                        }
                    }
                    callback.onProfilesFetched(profiles);
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error fetching profiles: " + e);
                    callback.onProfilesFetched(new ArrayList<>()); // Callback with empty list in case of failure
                });
    }

    /**
     * Retrieves & prints the list of all images.
     * US 04.06.01
     */
    public void browseImages() {
        // Browse profile pictures
        db.collection("Attendees").get()
                .addOnSuccessListener(attendeesSnapshots -> {
                    for (DocumentSnapshot attendeeSnapshot : attendeesSnapshots) {
                        if (attendeeSnapshot.contains("profile.profilePicture")) {
                            String profilePicUri = attendeeSnapshot.getString("profile.profilePicture.uriString");
                            if (profilePicUri != null) {
                                System.out.println("Attendee ID: " + attendeeSnapshot.getId() + ", Profile Picture URI: " + profilePicUri);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching profile pictures: " + e));
        // Browse event posters
        db.collection("events").get()
                .addOnSuccessListener(eventsSnapshots -> {
                    for (DocumentSnapshot eventSnapshot : eventsSnapshots) {
                        if (eventSnapshot.contains("poster")) {
                            String posterUri = eventSnapshot.getString("poster.uriString");
                            if (posterUri != null) {
                                System.out.println("Event ID: " + eventSnapshot.getId() + ", Poster URI: " + posterUri);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching event posters: " + e));
    }
}
