package com.example.qrcheckin;

import android.net.Uri;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class QRCode extends Image {
    private String hashedContent;

    public QRCode(String uriString, Attendee uploader, String unhashedContent) {
        super(uriString, uploader);
        this.hashedContent = hashString(unhashedContent);
    }

    public String getHashedContent() {
        return hashedContent;
    }

    public void setHashedContent(String hashedContent) {
        this.hashedContent = hashedContent;
    }

    /**
     * Put event details through a hashing function to generate a unique ID for it and it's QR code
     * @param unhashedContent Event details in the form of [name][date][time][location]
     * @return String SHA-256 ID which uniquely represents an event
     */
    private String hashString(String unhashedContent) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] encodedhash = digest.digest(unhashedContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            // Convert the hash into a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}