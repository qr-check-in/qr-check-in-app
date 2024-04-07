package com.example.qrcheckin;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.example.qrcheckin.Attendee.Profile;
import com.google.firebase.firestore.GeoPoint;

public class ProfileTest {

    private Profile profile;
    private GeoPoint geoPoint;

    @Before
    public void setUp() {
        profile = new Profile();
        geoPoint = new GeoPoint(-34.61315, -58.37723); // Example coordinates
    }

    @Test
    public void testGetInitials_WithName_ReturnsCorrectInitials() {
        profile.setName("John Doe");
        assertEquals("JD", profile.getInitials());
    }

    @Test
    public void testGetInitials_WithSingleName_ReturnsSingleInitial() {
        profile.setName("John");
        assertEquals("J", profile.getInitials());
    }

    @Test
    public void testGetInitials_WithEmptyName_ReturnsEmptyString() {
        profile.setName("");
        assertEquals("", profile.getInitials());
    }

    @Test
    public void testSetAndGetTrackGeolocation() {
        profile.setTrackGeolocation(true);
        assertTrue(profile.getTrackGeolocation());
    }

    @Test
    public void testSetAndGetLocation() {
        profile.setLocation(geoPoint);
        assertEquals(geoPoint, profile.getLocation());
    }

    @Test
    public void testSetAndGetName() {
        profile.setName("Jane Doe");
        assertEquals("Jane Doe", profile.getName());
    }

    @Test
    public void testSetAndGetHomepage() {
        profile.setHomepage("www.example.com");
        assertEquals("www.example.com", profile.getHomepage());
    }

    @Test
    public void testSetAndGetContact() {
        profile.setContact("jane@example.com");
        assertEquals("jane@example.com", profile.getContact());
    }
}
