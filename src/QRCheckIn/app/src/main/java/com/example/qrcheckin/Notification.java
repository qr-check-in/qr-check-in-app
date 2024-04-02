package com.example.qrcheckin;

import com.google.type.DateTime;

import org.checkerframework.checker.units.qual.Time;

import java.util.ArrayList;
import java.util.Date;

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
