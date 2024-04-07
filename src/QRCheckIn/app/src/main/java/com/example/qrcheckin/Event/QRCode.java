package com.example.qrcheckin.Event;


import com.example.qrcheckin.Attendee.Attendee;
import com.example.qrcheckin.Common.Image;
import com.example.qrcheckin.Common.Utils;

import java.io.Serializable;

public class QRCode extends Image implements Serializable {
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
     * @param isGenerated Boolean indicating if the QRCode was generated (instead of uploaded by user)
     */
    public QRCode(String uriString, Attendee uploader, String unhashedContent, Boolean isGenerated) {
        super(uriString, uploader);
        this.unhashContent = unhashedContent;
        if(isGenerated){
            this.hashedContent = Utils.hashString(unhashedContent);
        }
        else{
            // this QRCode represents an image uploaded by the user, do not hash the content read from the code
            this.hashedContent = unhashedContent;
        }

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