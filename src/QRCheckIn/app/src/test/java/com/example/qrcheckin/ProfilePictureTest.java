package com.example.qrcheckin;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qrcheckin.Attendee.Attendee;
import com.example.qrcheckin.Attendee.ProfilePicture;

public class ProfilePictureTest {

    @Test
    public void testConstructorAndGetters() {
        // Create an attendee for the uploader
        Attendee uploader = new Attendee();

        // Create a ProfilePicture object
        String uriString = "image_uri";
        ProfilePicture profilePicture = new ProfilePicture(uriString, uploader);

        // Verify that the constructor sets the URI and uploader correctly
        assertEquals(uriString, profilePicture.getUriString());
        assertEquals(uploader, profilePicture.getUploader());

        // Verify that isGenerated is initialized as null
        assertNull(profilePicture.getGenerated());
    }

    @Test
    public void testSetGenerated() {
        // Create an attendee for the uploader
        Attendee uploader = new Attendee();

        // Create a ProfilePicture object
        ProfilePicture profilePicture = new ProfilePicture();

        // Verify that isGenerated is initially null
        assertNull(profilePicture.getGenerated());

        // Set isGenerated to true
        profilePicture.setGenerated(true);
        assertTrue(profilePicture.getGenerated());

        // Set isGenerated to false
        profilePicture.setGenerated(false);
        assertFalse(profilePicture.getGenerated());
    }
}
