package com.example.qrcheckin;

import android.net.Uri;

import java.io.File;

public class EventPoster extends Image {
    private Attendee uploader;

    public EventPoster(String uriString, Attendee uploader) {
        super(uriString, uploader);
    }


}

//package com.example.qrcheckin;
//
// import android.media.Image;
//
// import java.io.File;
//
// public class EventPoster extends Image {
// private Event event;
// private Event event; // Association with the Event class
//
// // Constructor for creating a new EventPoster
// public EventPoster(File imageFile, Attendee uploader, Event event) {
// super(imageFile, uploader); // Call to the superclass (Image) constructor
// this.event = event;
// }
// }
