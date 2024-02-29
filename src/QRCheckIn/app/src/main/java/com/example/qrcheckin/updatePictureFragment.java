package com.example.qrcheckin;
import static androidx.camera.core.impl.utils.ContextUtil.getBaseContext;
import static androidx.core.content.ContextCompat.getMainExecutor;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Matrix;
import androidx.core.content.ContextCompat;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.ContextWrapper;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;


public class updatePictureFragment extends DialogFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_CODE = 1111;
    ConstraintLayout capture_layout, gallery_layout;
    int animationDuration;
    private ImageView imageView;
    ProcessCameraProvider cameraProvider;
    CameraSelector cameraSelector;
    PreviewView cam_preview;
    ImageCapture imageCapture;
    private String currentPhotoPath;
    ActivityResultLauncher<String> galleryGrab = registerForActivityResult(
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri == null) {
                        Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        // Assuming 'o' is meant to be 'uri' based on context
                        Bitmap imageBit = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                        attachToItem(imageBit); // Make sure variable names are consistent
                    } catch (FileNotFoundException e) {
                        // It's not a good practice to throw RuntimeException for a FileNotFoundException.
                        // Consider handling the exception more gracefully.
                        Log.e("GalleryExample", "File not found", e);
                        Toast.makeText(getActivity(), "File not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    public void attachToItem(Bitmap imageBit) {
        // Assuming there's an ImageView with the ID imageView in your layout
        ImageView imageView = getView().findViewById(R.id.imageView);
        imageView.setImageBitmap(imageBit);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.update_picture_fragment, null);

        ImageView cancelImageView = view.findViewById(R.id.cancel);
        TextView takePhotoTextView = view.findViewById(R.id.textView_takePhoto);
        TextView chooseGallery = view.findViewById(R.id.gallery);
        takePhotoTextView.setOnClickListener(v -> onCapturePressed());
        cancelImageView.setOnClickListener(v -> dismiss());
        chooseGallery.setOnClickListener(v ->onGalleryPressed());
        return builder.setView(view).create();
    }
    public void onGalleryPressed() {
        galleryGrab.launch("image/*");
    }
    public void onCapturePressed() {
        // check if app has permission to use the camera, else ask for permission
        String permission = Manifest.permission.CAMERA;
        if (EasyPermissions.hasPermissions(getActivity(), permission)) {
            animateCamera(true);
            ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(getActivity());
            cameraProviderListenableFuture.addListener(() -> {
                try {
                    cameraProvider = cameraProviderListenableFuture.get();
                    // Ensure this is called when getViewLifecycleOwner() is valid
                    if(isAdded()) { // Check if fragment is currently added to its activity
                        startCamera(cameraProvider);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    // Handle exception
                    Log.e("CameraXApp", "Error obtaining camera provider", e);
                }
            }, ContextCompat.getMainExecutor(getActivity()));
        } else {
            EasyPermissions.requestPermissions(this, "Our App Requires permission to access your camera", CAMERA_PERMISSION_CODE, permission);
        }
    }
    private void startCamera(ProcessCameraProvider cameraProvider) {
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cam_preview.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder().build();
        try {
            cameraProvider.unbindAll();

            cameraProvider.bindToLifecycle(getViewLifecycleOwner(),cameraSelector,preview,imageCapture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void animateCamera(boolean opening) {
        View open;
        View close;
        if (opening){
            open = capture_layout;
            close = gallery_layout;
        } else {
            open = gallery_layout;
            close = capture_layout;
        }

        open.setAlpha(0f);
        open.setVisibility(View.VISIBLE);

        open.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        close.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    /**
                     * @param animation The animation which reached its end.
                     */
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        close.setVisibility(View.GONE);
                    }
                });
    }

    private void capturePhoto() {
        if (imageCapture == null) return;

        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()), new ImageCapture.OnImageCapturedCallback() {
            /**
             * called on successful image capture
             * from here we can take the picture from memory and upload it to firebase
             *
             * @param image The captured image
             */
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                super.onCaptureSuccess(image);
                Toast.makeText(getActivity(), "Capture successful", Toast.LENGTH_SHORT).show();
                Bitmap image_bit = image.toBitmap();

                // must rotate bitmap 90 degrees to get correct orientation
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(image_bit, image.getWidth(), image.getHeight(), true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                attachToItem(rotatedBitmap);
                animateCamera(false);
            }
        });
    }
}