package com.example.qrcheckin.Attendee;

import com.example.qrcheckin.Common.Image;

public class ProfilePicture extends Image {
    //private Profile profile; // Reference to the associated profile
    private Boolean isGenerated;

    /**
     * Empty constructor for firebase purposes
     */
    public ProfilePicture(){}

    /**
     * Constructs a new ProfilePicture
     * @param uriString String of the Image's uri
     * @param uploader Attendee that uploaded the image
     */
    public ProfilePicture(String uriString, Attendee uploader) {
        super(uriString, uploader);
    }

    /**
     * Returns isGenerated, true if the profile's current profile picture generated from their initials
     * false if the profile picture was uploaded by the user from their camera roll
     * @return isGenerated boolean
     */
    public Boolean getGenerated() {
        return isGenerated;
    }

    /**
     * Sets isGenerated, true when the profile picture has been generated from initials
     * false when a user has uploaded a profile picture from camera roll
     * @param generated
     */
    public void setGenerated(Boolean generated) {
        isGenerated = generated;
    }
}
