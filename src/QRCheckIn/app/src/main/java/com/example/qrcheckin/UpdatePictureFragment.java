package com.example.qrcheckin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdatePictureFragment extends DialogFragment {
    private SharedViewModel sharedViewModel;
    private Uri currentPhotoUri;
    private String currentPhotoPath;

    // Permission request launcher


    // ActivityResultLaunchers for taking a picture and picking from the gallery

    private final ActivityResultLauncher<String> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    sharedViewModel.setSelectedImageUri(uri);
                    dismiss();
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
        ImageView cancelImageView = view.findViewById(R.id.cancel);
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