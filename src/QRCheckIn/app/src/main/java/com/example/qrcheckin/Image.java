package com.example.qrcheckin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Represents an image associated with an event or attendee within the QR Code Event Check-In system.
 */
public class Image {
    private File imageFile;
    private Attendee uploader;

    /**
     * Constructs an Image instance with specified image file and uploader.
     *
     * @param imageFile the image file associated with this Image instance.
     * @param uploader the attendee who uploaded the image.
     */
    public Image(File imageFile, Attendee uploader) {
        this.imageFile = imageFile;
        this.uploader = uploader;
    }

    /**
     * Gets the image file.
     *
     * @return the image file.
     */
    public File getImageFile() {
        return imageFile;
    }

    /**
     * Gets the uploader of the image.
     *
     * @return the uploader of the image.
     */
    public Attendee getUploader() {
        return uploader;
    }

    /**
     * Deletes the image file from the system.
     */
    public void deleteImage() {
        if (imageFile.exists()) {
            imageFile.delete();
        }
    }

    /**
     * Converts the image file to a BITMAP Base64 encoded string.
     *
     * @return a Base64 encoded string representing the image, or null if an error occurs.
     */
    public String encodeImageToBase64() {
        try (FileInputStream imageInputStream = new FileInputStream(imageFile)) {
            Bitmap bitmap = BitmapFactory.decodeStream(imageInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  Pending:
     *      uploading image to Firebase Storage
     *      updating  database with the image metadata
     */


}