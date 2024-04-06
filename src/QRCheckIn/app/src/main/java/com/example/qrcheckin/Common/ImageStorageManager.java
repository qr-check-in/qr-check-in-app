package com.example.qrcheckin.Common;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
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
     * Constructs an ImageStorageManager to deal with a file in storage, without an Image object
     * (For displaying the AddAdminQRCode without hardcoding it, in case it needs to be changed)
     * @param filePath String of the complete path and file name
     */
    public ImageStorageManager(String filePath){
        this.storage = FirebaseStorage.getInstance();
        this.filePath = filePath;
        this.storageReference = storage.getReference().child(filePath);
    }

    /**
     * Constructs an ImageStorageManager to deal with an Image object
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
     * @param imageView ImageView for cases where a profile picture has been removed, we need to re-call displayImage() once
     *                 the new profile picture has been successfully uploaded. Null in other cases.
     */
    public void uploadImage(ImageView imageView){
        Uri uri = Uri.parse(image.getUriString());
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess (UploadTask.TaskSnapshot taskSnapshot){
                Log.d("UPLOAD", "successfully uploaded image");
                if(imageView != null){
                    displayImage(imageView);
                }
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

    /**
     * Converts an image to a Bitmap
     * @param contentResolver ContentResolver of the calling activity
     * @return Bitmap of the image
     */
    public Bitmap convertToBitmap(ContentResolver contentResolver){
        Bitmap bitmap = null;
        Uri uri = Uri.parse(image.getUriString());
        // Converts Uri to Bitmap
        // https://stackoverflow.com/questions/65210522/how-to-get-bitmap-from-imageuri-in-api-level-30, HB., 2020
        try{
            if(Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
            }
            else{
                ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
