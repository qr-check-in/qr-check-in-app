package com.example.qrcheckin.Attendee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.qrcheckin.R;
import com.example.qrcheckin.Common.SharedViewModel;
import com.example.qrcheckin.Common.ImageStorageManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * Fragment for updating the profile picture.
 * Allows to select a new profile picture from the gallery.
 * If picture selected, updates SharedViewModel with the new image URI,
 * uploads the image to Firestore under "/ProfilePictures", and updates the user's profile picture.
 */
public class UpdatePictureFragment extends DialogFragment {
    private SharedViewModel sharedViewModel;
    private Uri currentPhotoUri;
    private String currentPhotoPath;
    private String fcmToken;
    /**
     * Constructs a new instance of UpdatePictureFragment with the specified FCM Token.
     * The FCM token is used to identify the user and update their profile picture information in the database.
     *
     * @param fcmToken The FCM token associated with the user.
     */
    public UpdatePictureFragment(String fcmToken){
        this.fcmToken = fcmToken;
    }


    // Permission request launcher


    // ActivityResultLaunchers for taking a picture and picking from the gallery

    private final ActivityResultLauncher<String> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    sharedViewModel.setSelectedImageUri(uri);
                    dismiss();

                    // Use ImageStorageManager to upload the image to firestore
                    ProfilePicture profilePicture = new ProfilePicture(uri.toString(), null);
                    ImageStorageManager storage = new ImageStorageManager(profilePicture,"/ProfilePictures");
                    storage.uploadImage(null);

                    // Use AttendeeDatabaseManager to delete the previous profile picture and replace it with the uploaded uri
                    AttendeeDatabaseManager dbManager = new AttendeeDatabaseManager(fcmToken);
                    dbManager.deleteProfilePicture(uri);

                }
            });

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_picture_fragment, null);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        setupViews(view);

        return builder.setView(view).create();
    }

    private void setupViews(View view) {
        Button cancelImageView = view.findViewById(R.id.cancel);
        TextView chooseGallery = view.findViewById(R.id.gallery);
        //TextView takePhoto = view.findViewById(R.id.textView_takePhoto);

        cancelImageView.setOnClickListener(v -> dismiss());

        chooseGallery.setOnClickListener(v -> selectImageLauncher.launch("image/*"));

        //takePhoto.setOnClickListener(v -> checkCameraPermissionAndOpen());
    }





    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}