package com.example.qrcheckin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class updatePictureFragment extends DialogFragment {
    private SharedViewModel sharedViewModel;
    private Uri currentPhotoUri;
    private String currentPhotoPath;

    private ImageView profileImageView;
    private final ActivityResultLauncher<String> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    sharedViewModel.setSelectedImageUri(uri);
                    dismiss();
                }
            });

    private final ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    sharedViewModel.setSelectedImageUri(currentPhotoUri);
                    dismiss();
                }
            });

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_picture_fragment, null);

        ImageView cancelImageView = view.findViewById(R.id.cancel);
        TextView chooseGallery = view.findViewById(R.id.gallery);
        //TextView takePhoto = view.findViewById(R.id.textView_takePhoto);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        cancelImageView.setOnClickListener(v -> dismiss());

        chooseGallery.setOnClickListener(v -> selectImageLauncher.launch("image/*"));

//        takePhoto.setOnClickListener(v -> {
//            try {
//                File photoFile = createImageFile();
//                currentPhotoUri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".fileprovider", photoFile);
//                takePictureLauncher.launch(currentPhotoUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
     return builder.setView(view).create();
//    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//    private void setPic() {
//        // Get the dimensions of the View
//        int targetW = profileImageView.getWidth();
//        int targetH = profileImageView.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
//        profileImageView.setImageBitmap(bitmap);
    }


}
