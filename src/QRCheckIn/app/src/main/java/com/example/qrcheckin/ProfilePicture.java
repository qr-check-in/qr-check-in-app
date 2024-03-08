package com.example.qrcheckin;

import android.net.Uri;

import java.io.File;

public class ProfilePicture extends Image{
    //private Profile profile; // Reference to the associated profile

    /**
     * Empty constructor for firebase purposes
     */
    public ProfilePicture(){}

    public ProfilePicture(String uriString, Attendee uploader) {
        super(uriString, uploader);

    }

    // Constructor

     public static File generateProfilePicture(Profile profile) {
        String profileName = profile.getName();
        String fileName = profileName + "_profile_picture.jpg";
        File profilePicture = new File(fileName);
        return profilePicture;
        }

}
