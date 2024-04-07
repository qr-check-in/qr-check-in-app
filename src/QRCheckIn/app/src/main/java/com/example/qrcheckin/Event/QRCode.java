package com.example.qrcheckin.Event;


import com.example.qrcheckin.Attendee.Attendee;
import com.example.qrcheckin.Common.Image;
import com.example.qrcheckin.Common.Utils;

public class QRCode extends Image {
    private String hashedContent;
    private String unhashContent;

    /**
     * empty constructor for firestore purposes
     */
    public QRCode(){}

    /**
     * Store a QR code with a generated ID linked to an event
     * @param uriString String uri representation of an image
     * @param uploader  Attendee object that uploaded the image
     * @param unhashedContent Event details string in the form of [name][date][time][location]
     */
    public QRCode(String uriString, Attendee uploader, String unhashedContent) {
        super(uriString, uploader);
        this.unhashContent = unhashedContent;
        this.hashedContent = Utils.hashString(unhashedContent);
    }

    public String getUnhashContent() {
        return unhashContent;
    }

    public void setUnhashContent(String unhashContent) {
        this.unhashContent = unhashContent;
    }

    public String getHashedContent() {
        return hashedContent;
    }

    public void setHashedContent(String hashedContent) {
        this.hashedContent = hashedContent;
    }

}