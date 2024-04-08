package com.example.qrcheckin;
import com.example.qrcheckin.Attendee.Attendee;
import com.example.qrcheckin.Event.Event;
import com.example.qrcheckin.Event.QRCode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qrcheckin.Event.EventPoster;

public class EventTest {
    private Event event;

    @Before
    public void setUp() {
        // Set up with dummy values
        Attendee uploader = new Attendee(); // Simplified for this example. Adjust according to your Attendee class constructor.
        String dummyContent = "EventName2024-04-1010:00AMEventLocation"; // Non-null unhashed content

        // Initialize QRCode objects with non-null unhashedContent
        QRCode checkInQRCode = new QRCode("CheckInQRCodeValue", uploader, dummyContent);
        QRCode promoQRCode = new QRCode("PromoQRCodeValue", uploader, dummyContent);

        // Initialize Event with QRCode objects that won't cause NullPointerException
        event = new Event(
                "OrganizerToken",
                checkInQRCode,
                promoQRCode,
                new EventPoster("PosterURL", uploader), // Adjust EventPoster constructor if necessary
                "Event Name",
                "2024-04-10",
                "10:00 AM",
                "Event Location",
                "Event Description",
                false,
                100
        );
    }

    @Test
    public void testEventProperties() {
        // Check initial values
        assertEquals("OrganizerToken", event.getOrganizer());
        assertEquals("CheckInQRCodeValue", event.getCheckInQRCode().getUriString());
        assertEquals("PromoQRCodeValue", event.getPromoQRCode().getUriString());
        assertEquals("PosterURL", event.getPoster().getUriString());
        assertEquals("Event Name", event.getEventName());
        assertEquals("2024-04-10", event.getEventDate());
        assertEquals("10:00 AM", event.getEventTime());
        assertEquals("Event Location", event.getEventLocation());
        assertEquals("Event Description", event.getEventDescription());
        assertFalse(event.isCheckInStatus());
        assertEquals(100, event.getSignupLimit());

        // Test setters
        event.setOrganizer("NewOrganizerToken");
        event.setEventName("New Event Name");
        event.setEventDate("2024-05-15");
        event.setEventTime("11:00 AM");
        event.setEventLocation("New Event Location");
        event.setEventDescription("New Event Description");
        event.setCheckInStatus(true);
        event.setSignupLimit(150);

        // Verify updated values
        assertEquals("NewOrganizerToken", event.getOrganizer());
        assertEquals("New Event Name", event.getEventName());
        assertEquals("2024-05-15", event.getEventDate());
        assertEquals("11:00 AM", event.getEventTime());
        assertEquals("New Event Location", event.getEventLocation());
        assertEquals("New Event Description", event.getEventDescription());
        assertTrue(event.isCheckInStatus());
        assertEquals(150, event.getSignupLimit());
    }

    // Additional tests can include testing the attendee and signup lists, notifications, and other functionalities specific to the Event class.
}
