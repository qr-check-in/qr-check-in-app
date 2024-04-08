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


















//package com.example.qrcheckin;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.example.qrcheckin.Attendee.Profile;
//import com.google.firebase.firestore.GeoPoint;
//
//public class ProfileTest {
//
//    String name = "John Doe", homepage = "https://www.SkilledForce.com", contact = "(555) 123-4567";
//    double latitude = 37.8199, longitude = -122.4783;
//
//    private Profile createDummyProfile() {
//        Profile profile = new Profile();
//
//        // Set some realistic data for profile details
//        profile.setName(name);
//        profile.setHomepage(homepage);
//        profile.setContact(contact);
//
//        // Set location to Golden Gate Bridge
//        GeoPoint location = new GeoPoint(latitude, longitude);
//        profile.setLocation(location);
//
//        // Set geolocation tracking permission
//        profile.setTrackGeolocation(true);
//
//        return profile;
//    }
//
//    @Test
//    void testCheckProfileDetails(){
//        Profile profile = createDummyProfile();
//
//        // Assert that profile details are correctly set
//        Assertions.assertEquals(name, profile.getName());
//        Assertions.assertEquals(homepage, profile.getHomepage());
//        Assertions.assertEquals(contact, profile.getContact()); // Verify Canadian phone number
//
//        // Assert location details
//        Assertions.assertNotNull(profile.getLocation());
//        Assertions.assertEquals(latitude, profile.getLocation().getLatitude());
//        Assertions.assertEquals(longitude, profile.getLocation().getLongitude());
//
//        // Assert geolocation tracking permission
//        Assertions.assertEquals(true, profile.getTrackGeolocation());
//    }
//}
