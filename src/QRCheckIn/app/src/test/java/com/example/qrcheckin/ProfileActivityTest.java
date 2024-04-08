package com.example.qrcheckin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import com.example.qrcheckin.Attendee.AttendeeDatabaseManager;
import com.example.qrcheckin.Attendee.Profile;
import com.example.qrcheckin.Attendee.ProfileActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ProfileActivityTest{

    @Mock
    private ProfileActivity profileActivity;
    @Mock
    private AttendeeDatabaseManager mockAttendeeDatabaseManager;

    private Profile profile;

    // Profile details in database
    private String nameInDatabase;
    private String contactInDatabase;
    private String homepageInDatabase;

    // Profile details user wants
    private String nameUpdated = "Michel";
    private String contactUpdated = "5877783799";
    private String homepageUpdated = "WWW.MichelSocial.ca";

    @Before
    public void setup(){
        mockAttendeeDatabaseManager = mock(AttendeeDatabaseManager.class);
        profileActivity = mock(ProfileActivity.class);

        // now database contain by default profile detials
        profile = new Profile();
        nameInDatabase = profile.getName();
        contactInDatabase = profile.getContact();
        homepageInDatabase = profile.getHomepage();
    }

    @Test
    public void testEditDetails()  {
        assertNotEquals(nameUpdated, nameInDatabase);
        assertNotEquals(contactUpdated, contactInDatabase);
        assertNotEquals(homepageUpdated, homepageInDatabase);

        profileActivity.editDetails(nameUpdated, contactUpdated, homepageUpdated);

        // Stubbing the updateProfileString method to update contactInDatabase variable
        doAnswer(invocation -> {
            String field = invocation.getArgument(0);
            String value = invocation.getArgument(1);
            if ("name".equals(field)) {
                nameInDatabase = value;
                assertEquals(nameUpdated, nameInDatabase);
            }
            return null;
        }).when(mockAttendeeDatabaseManager).updateProfileString("name", nameUpdated, null);

        // Stubbing the updateProfileString method to update contactInDatabase variable
        doAnswer(invocation -> {
            String field = invocation.getArgument(0);
            String value = invocation.getArgument(1);
            if ("contact".equals(field)) {
                contactInDatabase = value;
                assertEquals(contactUpdated, contactInDatabase);
            }
            return null;
        }).when(mockAttendeeDatabaseManager).updateProfileString("contact", contactUpdated, null);

        // Stubbing the updateProfileString method to update contactInDatabase variable
        doAnswer(invocation -> {
            String field = invocation.getArgument(0);
            String value = invocation.getArgument(1);
            if ("homepage".equals(field)) {
                homepageInDatabase = value;
                assertEquals(homepageUpdated, homepageInDatabase);
            }
            return null;
        }).when(mockAttendeeDatabaseManager).updateProfileString("homepage", homepageUpdated, null);

    }
}
