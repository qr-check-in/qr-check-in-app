package com.example.qrcheckin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Represents an image associated with an event or attendee within the QR Code Event Check-In system.
 */
public class Image {
    //private File imageFile;
    private Attendee uploader;
    private Uri imageUri;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageReference = storage.getReference();
    LinearProgressIndicator progress;
    /**
     * Constructs an Image instance with specified image file and uploader.
     *
     * @param imageUri Uri of the image
     * @param uploader the attendee who uploaded the image.
     */
    public Image(Uri imageUri, Attendee uploader) {
        this.imageUri = imageUri;
        this.uploader = uploader;
    }

    /**
     * Gets the image file.
     *
     * @return the image file.
     */
    public Uri getImageUri() {
        return imageUri;
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

    }

    /**
     * Converts the image file to a BITMAP Base64 encoded string.
     *
     * @return a Base64 encoded string representing the image, or null if an error occurs.
     */

    public void uploadImage(String folderName, String fileName){
        StorageReference reference = storageReference.child(folderName+fileName);
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess (UploadTask.TaskSnapshot taskSnapshot){

            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e){

            }
        });
        }
    }





