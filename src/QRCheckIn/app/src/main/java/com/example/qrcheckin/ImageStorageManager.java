package com.example.qrcheckin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * Deals with storing image files in Firebase Storage and retrieving them to be displayed
 */
public class ImageStorageManager {
    private final FirebaseStorage storage;
    private StorageReference storageReference;
    private Image image;
    private String filePath;

    /**
     * Constructs an ImageStorageManager
     * @param image Image to be stored/deleted/displayed by manager's methods
     * @param folderName String of the Image's location in firebase storage
     */
    public ImageStorageManager(Image image, String folderName){
        this.storage = FirebaseStorage.getInstance();
        this.image = image;
        this.filePath = folderName+"/"+image.getUriString();
        this.storageReference = storage.getReference().child(filePath);
    }

    /**
     * Uploads a Uri to firestorage
     */
    public void uploadImage(){
        Uri uri = Uri.parse(image.getUriString());
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
     * Retrieves an Image file from FireStorage, converts to a bitmap and sets the input ImageView to display it
     * @param imageView ImageView that the file is to be displayed on
     */
    public void displayImage(ImageView imageView){
        try{
            final File localFile = File.createTempFile("tempPic", "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Convert local file to bitmap and set the imageview
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Firestore", "picture display error");
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Deletes an image file from firebase storage
     */
    public void deleteImage(){
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "picture deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "picture deletion error");
            }
        });
    }
}
