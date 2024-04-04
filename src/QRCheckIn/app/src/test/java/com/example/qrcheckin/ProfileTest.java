package com.example.qrcheckin;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import com.example.qrcheckin.Attendee.AttendeeDatabaseManager;
import com.example.qrcheckin.Attendee.ProfileActivity;
import com.example.qrcheckin.ClassObjects.Attendee;
import com.example.qrcheckin.ClassObjects.Profile;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

public class ProfileTest {

    String name = "John Doe", homepage = "https://www.SkilledForce.com", contact = "(555) 123-4567";
    double latitude = 37.8199, longitude = -122.4783;

    private Profile createDummyProfile() {
        Profile profile = new Profile();

        // Set some realistic data for profile details
        profile.setName(name);
        profile.setHomepage(homepage);
        profile.setContact(contact);

        // Set location to Golden Gate Bridge
        GeoPoint location = new GeoPoint(latitude, longitude);
        profile.setLocation(location);

        // Set geolocation tracking permission
        profile.setTrackGeolocation(true);

        return profile;
    }

    @Test
    void testCheckProfileDetails(){
        Profile profile = createDummyProfile();

        // Assert that profile details are correctly set
        assertEquals(name, profile.getName());
        assertEquals(homepage, profile.getHomepage());
        assertEquals(contact, profile.getContact()); // Verify Canadian phone number

        // Assert location details
        assertNotNull(profile.getLocation());
        assertEquals(latitude, profile.getLocation().getLatitude());
        assertEquals(longitude, profile.getLocation().getLongitude());

        // Assert geolocation tracking permission
        assertEquals(true, profile.getTrackGeolocation());
    }

    @Test
    void testEditDetails() {
//        // Create an instance of ProfileActivity
//        ProfileActivity profileActivity = new ProfileActivity();
//
//        // Create a mock AttendeeDatabaseManager
//        AttendeeDatabaseManager mockDbManager = Mockito.mock(AttendeeDatabaseManager.class);
//
//        // Create a mock DocumentSnapshot
//        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
//
//        // Create a mock Attendee object
//        Attendee mockAttendee = Mockito.mock(Attendee.class);
//
//        // Create a mock Profile object
//        Profile mockProfile = Mockito.mock(Profile.class);
//
//        // Set up the mock behavior for the DocumentSnapshot and Attendee
//        Mockito.when(mockDocumentSnapshot.toObject(Attendee.class)).thenReturn(mockAttendee);
//        Mockito.when(mockAttendee.getProfile()).thenReturn(mockProfile);

    }
}
