package com.example.qrcheckin.Event;

import com.example.qrcheckin.Attendee.Attendee;
import com.example.qrcheckin.Common.Image;

import java.io.Serializable;

/**
 * Represents a poster for an event includes specific details.
 */
public class EventPoster extends Image implements Serializable {
    private Attendee uploader;

    /**
     * Empty constructor for firestore purposes
     */
    public EventPoster(){}

    /**
     * Makes EventPoster specifying URI and uploader.
     *
     * @param uriString The URI of the image for the event poster.
     * @param uploader The attendee who uploaded the event poster.
     */
    public EventPoster(String uriString, Attendee uploader) {
        super(uriString, uploader);
    }

}


