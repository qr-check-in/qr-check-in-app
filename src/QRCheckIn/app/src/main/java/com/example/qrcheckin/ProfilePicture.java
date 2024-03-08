package com.example.qrcheckin;

import android.net.Uri;

import java.io.File;
/**
 * abstract profile picture associated with a user's profile.
 */
public abstract class ProfilePicture extends Image {
    private Profile profile; // Reference to the associated profile
    /**
     * Constructs ProfilePicture object associated with a given profile and uploaded by a specific attendee.
     *
     * @param imageUri The URI of the image file.
     * @param uploader The attendee who uploaded the profile picture.
     */
    public ProfilePicture(Uri imageUri, Attendee uploader) {
        super(imageUri, uploader);

public class ProfilePicture extends Image{
    //private Profile profile; // Reference to the associated profile

    /**
     * Empty constructor for firebase purposes
     */
    public ProfilePicture(){}

    public ProfilePicture(String uriString, Attendee uploader) {
        super(uriString, uploader);

    }

    /**
     * File for storing profile picture based on the profile info.
     * File name is derived from the profile's name & includes a suffix to denote it as profile picture.
     *
     * @param profile The profile for which the profile picture is to be generated.
     * @return An object representing the generated profile picture file.
     */
    // Constructor

     public static File generateProfilePicture(Profile profile) {
        String profileName = profile.getName();
        String fileName = profileName + "_profile_picture.jpg";
        File profilePicture = new File(fileName);
        return profilePicture;
        }

}
