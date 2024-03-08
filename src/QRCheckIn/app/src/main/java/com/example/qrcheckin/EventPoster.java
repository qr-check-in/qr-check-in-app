package com.example.qrcheckin;

import android.net.Uri;

import java.io.File;
/**
 * Represents a poster for an event includes specific details.
 */
public class EventPoster extends Image {
    private Attendee uploader;
    /**
     * Makes EventPoster specifying URI and uploader.
     *
     * @param imageUri The URI of the image for the event poster.
     * @param uploader The attendee who uploaded the event poster.
     */
    public EventPoster(Uri imageUri, Attendee uploader) {
        super(imageUri, uploader);
    }


}


