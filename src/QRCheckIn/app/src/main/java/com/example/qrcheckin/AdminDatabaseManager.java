package com.example.qrcheckin;
import com.example.qrcheckin.Common.DatabaseManager;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

public class AdminDatabaseManager extends DatabaseManager {

    public AdminDatabaseManager(String collectionName) {
        super(collectionName);
    }

    public AdminDatabaseManager(String collectionName, String documentID) {
        super(collectionName, documentID);
    }

    /**
     * Retrieves & prints details of an event.
     *
     * @param eventId the ID of the event to view.
     */
    public void viewEvent(String eventId) {
        FirebaseFirestore.getInstance().collection("events").document(eventId).get()
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
     *
     * @param eventId the ID of the event to delete.
     */
    public void deleteEvent(String eventId) {
        FirebaseFirestore.getInstance().collection("events").document(eventId).delete()
                .addOnSuccessListener(aVoid -> System.out.println("Event successfully deleted!"))
                .addOnFailureListener(e -> System.out.println("Error deleting event: " + e));
    }

    /**
     * Retrieves and prints the details of a user profile.
     *
     * @param userId the ID of the user profile to view.
     */
    public void viewProfile(String userId) {
        FirebaseFirestore.getInstance().collection("Attendees").document(userId).get()
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
     *
     * @param userId the ID of the user profile to delete.
     */
    public void deleteProfile(String userId) {
        FirebaseFirestore.getInstance().collection("Attendees").document(userId)
                .update("profile", FieldValue.delete())
                .addOnSuccessListener(aVoid -> System.out.println("Profile successfully deleted!"))
                .addOnFailureListener(e -> System.out.println("Error deleting profile: " + e));
    }

    /**
     * Retrieves and prints the details of an image, which could be a profile picture or event poster.
     *
     * @param documentId The ID of the document (either an Attendee or an Event).
     * @param imageType  The type of image to view ("profilePicture" or "poster").
     */
    public void viewImage(String documentId, String imageType) {
        String collectionPath = "profilePicture".equals(imageType) ? "Attendees" : "events";
        String fieldPath = "profilePicture".equals(imageType) ? "profile.profilePicture.uriString" : "poster.uriString";

        FirebaseFirestore.getInstance().collection(collectionPath).document(documentId).get()
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
     *
     * @param documentId The ID of the document (either an Attendee or an Event).
     * @param imageType  The type of image to delete ("profilePicture" or "poster").
     */
    public void deleteImage(String documentId, String imageType) {
        // Determine the collection and field path based on the image type
        String collectionPath = "profilePicture".equals(imageType) ? "Attendees" : "events";
        String fieldPath = "profilePicture".equals(imageType) ? "profile.profilePicture" : "poster";

        // Perform the update to remove the image field
        FirebaseFirestore.getInstance().collection(collectionPath).document(documentId)
                .update(fieldPath, FieldValue.delete())
                .addOnSuccessListener(aVoid -> System.out.println(imageType + " successfully deleted."))
                .addOnFailureListener(e -> System.out.println("Error deleting " + imageType + ": " + e));
    }

    /**
     * Retrieves & prints the list of all events.
     */
    public void browseEvents() {
        FirebaseFirestore.getInstance().collection("events").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        System.out.println("Event ID: " + documentSnapshot.getId() + ", Data: " + documentSnapshot.getData());
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching events: " + e));
    }

    /**
     * Retrieves & prints the list of all profiles.
     */
    public void browseProfiles() {
        FirebaseFirestore.getInstance().collection("Attendees").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        System.out.println("Profile ID: " + documentSnapshot.getId() + ", Data: " + documentSnapshot.getData());
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching profiles: " + e));
    }

    /**
     * Retrieves & prints the list of all images (profile pictures and event posters).
     */
    public void browseImages() {
        // Browse profile pictures
        FirebaseFirestore.getInstance().collection("Attendees").get()
                .addOnSuccessListener(attendeesSnapshots -> {
                    for (QueryDocumentSnapshot attendeeSnapshot : attendeesSnapshots) {
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
        FirebaseFirestore.getInstance().collection("events").get()
                .addOnSuccessListener(eventsSnapshots -> {
                    for (QueryDocumentSnapshot eventSnapshot : eventsSnapshots) {
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

