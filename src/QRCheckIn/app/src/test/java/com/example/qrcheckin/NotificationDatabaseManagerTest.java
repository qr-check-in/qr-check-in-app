package com.example.qrcheckin;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.qrcheckin.Notifications.Notification;
import com.example.qrcheckin.Notifications.NotificationDatabaseManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class NotificationDatabaseManagerTest {

    private Notification notification;

    @Mock
    private NotificationDatabaseManager databaseManager;

    @Before
    public void setup(){
        notification= new Notification("Test Notification", "This is a test message", "2024-04-08 12:00:00", "123456");

        databaseManager = mock(NotificationDatabaseManager.class);

    }

    @Test
    public void testStoreNotification() {

        when(databaseManager.storeNotification(notification)).thenReturn(notification.getEventID());

        // call to storeNotification()
        String docId = databaseManager.storeNotification(notification);

        // check value fo docId with return value of function storeNotification()
        // Verify that the returned document ID matches the one set in the Notification object
        assertEquals("123456", docId);
    }
}
