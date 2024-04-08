package com.example.qrcheckin.Attendee;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import android.util.Log;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ProfileTest{

    private ProfileActivity profileActivity;
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
        profileActivity = new ProfileActivity();

        profile = new Profile();

    }

    @Test
    public void testEditDetails()  {
        // Stubbing the updateProfileString method to update contactInDatabase variable
        doAnswer(invocation -> {
            String field = invocation.getArgument(0);
            String value = invocation.getArgument(1);
            if ("name".equals(field)) {
                nameInDatabase = value;
            }
            return null; // Stubbing a void method, so return null
        }).when(mockAttendeeDatabaseManager).updateProfileString("name", nameUpdated, any());

        // Stubbing the updateProfileString method to update contactInDatabase variable
        doAnswer(invocation -> {
            String field = invocation.getArgument(0);
            String value = invocation.getArgument(1);
            if ("contact".equals(field)) {
                contactInDatabase = value;
            }
            return null; // Stubbing a void method, so return null
        }).when(mockAttendeeDatabaseManager).updateProfileString("contact", contactUpdated, any());


        // Stubbing the updateProfileString method to update contactInDatabase variable
        doAnswer(invocation -> {
            String field = invocation.getArgument(0);
            String value = invocation.getArgument(1);
            if ("homepage".equals(field)) {
                homepageInDatabase = value;
            }
            return null; // Stubbing a void method, so return null
        }).when(mockAttendeeDatabaseManager).updateProfileString("homepage", homepageUpdated, any());

        // Call your method here
        // For example:
        // profileActivity.someMethodToUpdateProfile();
        profileActivity.editDetails(nameUpdated, contactUpdated, homepageUpdated);

        // Assert the updated value
        assertEquals(nameUpdated, nameInDatabase);
//        assertEquals(contactUpdated, contactInDatabase);
//        assertEquals(homepageUpdated, homepageInDatabase);
    }

}