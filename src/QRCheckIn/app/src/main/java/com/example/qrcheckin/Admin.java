package com.example.qrcheckin;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

/**
 * Admin class responsible for administrative actions such as viewing and deleting events, profiles, and images.
 */

public class Admin extends Attendee {
    private AdminDatabaseManager dbManager;

    /**
     * Constructor for admin initializes the database manager instance.
     */
    public Admin() {
        // Initialize AdminDatabaseManager with a specific collection if needed,
        // or use methods directly for different collections.
        this.dbManager = new AdminDatabaseManager("");
    }

    /**
     * Views details of an event.
     *
     * @param eventId the ID of the event to view.
     */
    public void viewEvent(String eventId) {
        dbManager.viewEvent(eventId);
    }

    /**
     * Deletes an event from the database.
     *
     * @param eventId the ID of the event to delete.
     */
    public void deleteEvent(String eventId) {
        dbManager.deleteEvent(eventId);
    }

    /**
     * Views details of a user profile.
     *
     * @param userId the ID of the user profile to view.
     */
    public void viewProfile(String userId) {
        dbManager.viewProfile(userId);
    }

    /**
     * Deletes a user profile from the database.
     *
     * @param userId the ID of the user profile to delete.
     */
    public void deleteProfile(String userId) {
        dbManager.deleteProfile(userId);
    }

    /**
     * Views details of an image (profile picture or event poster).
     *
     * @param documentId The ID of the document (either an Attendee or an Event).
     * @param imageType  The type of image to view ("profilePicture" or "poster").
     */
    public void viewImage(String documentId, String imageType) {
        dbManager.viewImage(documentId, imageType);
    }

    /**
     * Deletes an image from the database.
     *
     * @param documentId The ID of the document (either an Attendee or an Event).
     * @param imageType  The type of image to delete ("profilePicture" or "poster").
     */
    public void deleteImage(String documentId, String imageType) {
        dbManager.deleteImage(documentId, imageType);
    }

    /**
     * Retrieves & prints the list of all events.
     */
    public void browseEvents() {
        dbManager.browseEvents();
    }

    /**
     * Retrieves & prints the list of all profiles.
     */
    public void browseProfiles() {
        dbManager.browseProfiles();
    }

    /**
     * Retrieves & prints the list of all images (profile pictures and event posters).
     */
    public void browseImages() {
        dbManager.browseImages();
    }
}

