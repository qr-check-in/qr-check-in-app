package com.example.qrcheckin;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * Admin class responsible for administrative actions such as viewing and deleting events, profiles, and images.
 *** SHOULD EXTEND ATTENDEE BUT WAITING ON ATTENDEE CLASS TO BE CREATED!***
 */

public class Admin { //extends attendee
    private final FirebaseFirestore db;

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
                        System.out.println("No such event!");
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
        db.collection("profiles").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        System.out.println("Profile data: " + documentSnapshot.getData());
                    } else {
                        System.out.println("No such profile!");
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
        db.collection("profiles").document(userId).delete()
                .addOnSuccessListener(aVoid -> System.out.println("Profile successfully deleted!"))
                .addOnFailureListener(e -> System.out.println("Error deleting profile: " + e));
    }

    /**
     * Retrieves and prints the details of an image.
     *
     * @param imageId the ID of the image to view.
     */
    public void viewImage(String imageId) {
        db.collection("images").document(imageId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        System.out.println("Image data: " + documentSnapshot.getData());
                    } else {
                        System.out.println("No such image!");
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching image: " + e));
    }

    /**
     * Deletes an image from the database.
     * US 04.03.01
     *
     * @param imageId the ID of the image to delete.
     */
    public void deleteImage(String imageId) {
        // Delete an image from the 'images' collection
        db.collection("images").document(imageId).delete()
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Image successfully deleted!");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error deleting image: " + e);
                });
    }

    /**
     * Retrieves & prints the list of all events.
     * US 04.04.01
     */
    public void browseEvents() {
        db.collection("events").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        System.out.println("Event ID: " + documentSnapshot.getId() + ", Data: " + documentSnapshot.getData());
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching events: " + e));
    }

    /**
     * Retrieves & prints the list of all profiles.
     * US 04.05.01.
     */
    public void browseProfiles() {
        db.collection("profiles").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        System.out.println("Profile ID: " + documentSnapshot.getId() + ", Data: " + documentSnapshot.getData());
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching profiles: " + e));
    }

    /**
     * Retrieves & prints the list of all images.
     * US 04.06.01
     */
    public void browseImages() {
        db.collection("images").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        System.out.println("Image ID: " + documentSnapshot.getId() + ", Data: " + documentSnapshot.getData());
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching images: " + e));
    }

}
