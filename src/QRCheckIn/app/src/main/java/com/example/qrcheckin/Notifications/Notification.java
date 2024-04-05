package com.example.qrcheckin.Notifications;

/**
 * Represents a notification message intended for multiple recipients.
 */
public class Notification {
    // notification title
    private String title;
    // Notification Description
    private String message;
    // Notification date
    private String dateTime;
    // event id
    private String eventID;

    /**
     * Empty constructor for firestore purposes
     */
    public Notification(){}

    public Notification(String title, String message, String dateTime, String eventID) {
        this.title = title;
        this.message = message;
        this.dateTime = dateTime;
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
