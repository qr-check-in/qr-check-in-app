package com.example.qrcheckin.Common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class providing constants and utility methods related to FCM.
 * Contains constants that are used throughout the app for consistency in FCM operations.
 */
public class Utils {
    // Tag used to identify the log message sent by MyFirebaseMessagingService
    public static final String TAG = "MyFirebaseMessagingService";

    /**
     * Put event details through a hashing function to generate a unique ID for it and it's QR code
     * @param inputString Event details in the form of [name][date][time][location]
     * @return String SHA-256 ID which uniquely represents an event
     */
    public static String hashString(String inputString) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] encodedhash = digest.digest(inputString.getBytes(java.nio.charset.StandardCharsets.UTF_8));

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
