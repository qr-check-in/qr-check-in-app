package com.example.qrcheckin;

import android.net.Uri;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class QRCode extends Image {
    private String hashedContent;

    /**
     * Store a QR code with a generated ID linked to an event
     * @param uriString String uri representation of an image
     * @param uploader  Attendee object that uploaded the image
     * @param unhashedContent Event details string in the form of [name][date][time][location]
     */
    public QRCode(Uri uriString, Attendee uploader, String unhashedContent) {
        super(uriString, uploader);
        this.hashedContent = Utils.hashString(unhashedContent);
    }

    public String getHashedContent() {
        return hashedContent;
    }

    public void setHashedContent(String hashedContent) {
        this.hashedContent = hashedContent;
    }

}