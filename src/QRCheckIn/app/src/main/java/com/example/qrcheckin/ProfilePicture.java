package com.example.qrcheckin;
import java.io.File;

public abstract class ProfilePicture extends Image {
    private Profile profile; // Reference to the associated profile

    public ProfilePicture(File imageFile, Attendee uploader) {
        super();
        this.profile = profile;
    }

    // Constructor

     public static File generateProfilePicture(Profile profile) {
        String profileName = profile.getName();
        String fileName = profileName + "_profile_picture.jpg";
        File profilePicture = new File(fileName);
        return profilePicture;
        }

}
