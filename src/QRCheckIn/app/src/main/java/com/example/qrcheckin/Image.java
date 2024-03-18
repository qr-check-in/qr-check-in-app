package com.example.qrcheckin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * Represents an image associated with an event or attendee within the QR Code Event Check-In system.
 * Also contains methods to store and retrieve the associated image files in Firestore Storage
 */
public class Image{
    //private File imageFile;
    private Attendee uploader;
    private String uriString;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageReference = storage.getReference();
    LinearProgressIndicator progress;
    /**
     * Constructs an Image instance with specified image file and uploader.
     *
     * @param uriString Uri of the image
     * @param uploader the attendee who uploaded the image.
     */
    public Image(String uriString, Attendee uploader) {
        this.uriString = uriString;
        this.uploader = uploader;
    }

    /**
     * Empty constructor for firebase
     */
    public Image(){}

    /**
     * Gets the image file.
     *
     * @return the image file.
     */
    public String getUriString() {
        return uriString;
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

    /**
     * Uploads a Uri to firestorage
     * @param folderName String folder the file is saved to in format "/FolderName"
     * @param fileName String name of the file
     */
    public void uploadImage(String folderName, String fileName){
        StorageReference reference = storageReference.child(folderName+"/"+fileName);
        Uri uri = Uri.parse(uriString);
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess (UploadTask.TaskSnapshot taskSnapshot){

            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e){

            }
        });
        }

    /**
     * Retrieves a file from FireStorage, converts to a bitmap and sets the input ImageView to display it
     * @param folder String of the folder where the image file is stored in firestore storage
     * @param imageView ImageView that the file is to be displayed on
     */
    public void displayImage(String folder, ImageView imageView){
        // Create string of the path to the image file in firestorage
        String filePath = folder+this.uriString;
        // Get the file
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child(filePath);
        try{
            final File localFile = File.createTempFile("tempPic", "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Firestore", "picture retrieved");
                    // Convert local file to bitmap and set the imageview
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Firestore", "picture error");
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}





