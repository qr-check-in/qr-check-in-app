package com.example.qrcheckin;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a notification message intended for multiple recipients.
 */
public class Notification {

    /**
     * The notification message.
     */
    private String message;

    /**
     * The list of recipients for the notification.
     */
    private ArrayList<Attendee> recipients;

    private Date notificationDate;

}
