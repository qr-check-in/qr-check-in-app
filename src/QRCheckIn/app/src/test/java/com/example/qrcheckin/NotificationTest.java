package com.example.qrcheckin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.example.qrcheckin.Notifications.Notification;

public class NotificationTest {

    @Test
    public void testConstructorAndGetters() {
        // Given
        String title = "Test Notification";
        String message = "This is a test message";
        String dateTime = "2024-04-08 12:00:00";
        String eventID = "123456";

        // When
        Notification notification = new Notification(title, message, dateTime, eventID);

        // Then
        assertEquals(title, notification.getTitle());
        assertEquals(message, notification.getMessage());
        assertEquals(dateTime, notification.getDateTime());
        assertEquals(eventID, notification.getEventID());
    }

    @Test
    public void testSetters() {
        // Given
        Notification notification = new Notification();
        String title = "New Test Notification";
        String message = "This is a new test message";
        String dateTime = "2024-04-09 12:00:00";
        String eventID = "654321";

        // When
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setDateTime(dateTime);
        notification.setEventID(eventID);

        // Then
        assertEquals(title, notification.getTitle());
        assertEquals(message, notification.getMessage());
        assertEquals(dateTime, notification.getDateTime());
        assertEquals(eventID, notification.getEventID());
    }
}
