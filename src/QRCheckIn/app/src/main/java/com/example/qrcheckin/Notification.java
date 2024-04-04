package com.example.qrcheckin;

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
    // List of recipients

    /**
     *
     * @param title
     * @param message
     */
    public Notification(String title, String message) {
        this.title = title;
        this.message = message;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
