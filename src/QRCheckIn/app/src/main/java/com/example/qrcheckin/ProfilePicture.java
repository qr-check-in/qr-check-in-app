package com.example.qrcheckin;

public class ProfilePicture extends Image{
    //private Profile profile; // Reference to the associated profile

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
}
