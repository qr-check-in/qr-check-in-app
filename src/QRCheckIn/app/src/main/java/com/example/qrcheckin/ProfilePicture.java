package com.example.qrcheckin;

import android.media.Image;

import java.io.File;

public class ProfilePicture extends Image {
    private Profile profile; // Reference to the associated profile

    // Constructor
    public ProfilePicture(File imageFile, Attendee uploader, Profile profile) {
        super(imageFile, uploader); // Calling the constructor of the superclass Image
        this.profile = profile;
    }
     public static File generateProfilePicture(Profile profile) {
        String profileName = profile.getName();
        String fileName = profileName + "_profile_picture.jpg";
        File profilePicture = new File(fileName);
        return profilePicture;
        }
}
