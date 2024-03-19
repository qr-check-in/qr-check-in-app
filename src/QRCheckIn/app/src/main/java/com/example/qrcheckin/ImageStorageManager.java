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
    StorageReference storageReference;

    /**
     * Constructs an ImageStorageManager object
     */
    public ImageStorageManager(){
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
    }

    /**
     * Uploads a Uri to firestorage
     * @param image Image to be stored as a uri
     * @param folderName String folder the file is saved to in format "/FolderName"
     */
    public void uploadImage(Image image, String folderName){
        StorageReference reference = storageReference.child(folderName+"/"+image.getUriString());
        Uri uri = Uri.parse(image.getUriString());
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
     * Retrieves an Image file from FireStorage, converts to a bitmap and sets the input ImageView to display it
     * @param image Image to be displayed
     * @param folder String of the folder where the image file is stored in firestore storage
     * @param imageView ImageView that the file is to be displayed on
     */
    public void displayImage(Image image, String folder, ImageView imageView){
        // Create string of the path to the image file in firestorage
        String filePath = folder+image.getUriString();
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
