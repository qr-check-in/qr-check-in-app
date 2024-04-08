package com.example.qrcheckin.Event;
import android.media.metrics.Event;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Organizer class responsible for managing events.
 *  *** SHOULD EXTEND ATTENDEE BUT WAITING ON ATTENDEE CLASS TO BE CREATED!***
 */

public class Organizer { //extends attendee *********
    private final FirebaseFirestore db;
    private List<Event> events; // ***** Waiting on event class to be created *****

    /**
     * Constructor for organizer initializes firestore instance.
     */
    public Organizer() {
        db = FirebaseFirestore.getInstance();
        this.events = new ArrayList<>();
    }

    /**
     * Creates a new event with a title and location.
     * US 01.01.01
     *
     * @param title    the title of the event
     * @param location the location of the event
     */
    public void createEvent(String title, String location) {
        Map<String, Object> event = new HashMap<>();
        event.put("title", title);
        event.put("location", location);
        event.put("attendees", new ArrayList<String>());

        db.collection("events")
                .add(event)
                .addOnSuccessListener(documentReference -> System.out.println("Event created with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> System.out.println("Error adding event: " + e));
    }

    /**
     * Generates a QR code for a given event.
     *
     * @param eventId the ID of the event
     */
    public void generateQRCode(String eventId) {
        String qrCodeData = eventId;

        db.collection("events").document(eventId)
                .update("qrCodeData", qrCodeData)
                .addOnSuccessListener(aVoid -> System.out.println("QR Code added to event: " + eventId))
                .addOnFailureListener(e -> System.out.println("Error updating event with QR code: " + e));
    }

    /**
     * View the list of attendees who have checked into an event.
     * US 01.02.01
     *
     * @param eventId the ID of the event for which to view checked-in attendees
     */
    public void viewCheckIns(String eventId) {
        db.collection("events").document(eventId).collection("checkIns")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        System.out.println("Checked-in attendee: " + documentSnapshot.getId());
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error fetching check-ins: " + e));
    }

    /**
     * Generates a promotional QR code for a given event.
     * US 01.07.01
     * commented out until Event class created and getID is implemented in Event class.
     *
     * @param event the event for which to generate a promo QR code
     */
//    public void generatePromoQR(Event event) {
/////////// Assuming Event has a getId method to fetch its unique identifier
//        String promoQRCodeData = "PROMO_" + event.getId();

//        db.collection("events").document(event.getId())
//                .update("promoQRCodeData", promoQRCodeData)
//                .addOnSuccessListener(aVoid -> System.out.println("Promo QR Code added to event: " + event.getId()))
//                .addOnFailureListener(e -> System.out.println("Error updating event with Promo QR code: " + e));}



    /**
     * Sends a notification message related to an event.
     * US 01.03.01
     *
     * @param eventId the ID of the event
     * @param message the message to be sent
     */
    public void sendNotification(String eventId, String message) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("message", message);
        notification.put("eventId", eventId);

        db.collection("notifications")
                .add(notification)
                .addOnSuccessListener(documentReference -> System.out.println("Notification sent for event " + eventId))
                .addOnFailureListener(e -> System.out.println("Error sending notification: " + e));
    }

    /**
     * Uploads an event poster and associates it with an event.
     * US 01.04.01
     *
     * @param eventId    the ID of the event
     * @param posterPath the file path of the poster image
     */
    public void uploadEventPoster(String eventId, String posterPath) {
        db.collection("events").document(eventId)
                .update("posterPath", posterPath)
                .addOnSuccessListener(aVoid -> System.out.println("Poster path updated for event: " + eventId))
                .addOnFailureListener(e -> System.out.println("Error updating event with poster path: " + e));
    }






}
