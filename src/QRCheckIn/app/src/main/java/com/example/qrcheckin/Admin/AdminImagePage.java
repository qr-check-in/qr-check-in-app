package com.example.qrcheckin.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrcheckin.Common.Image;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qrcheckin.Attendee.ProfileActivity;
import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.Common.MainActivity;
import com.example.qrcheckin.Event.CreateAddEventDetails;
import com.example.qrcheckin.Event.EventListView;
import com.example.qrcheckin.R;

/**
 * An activity for displaying a full-sized image in the admin section,
 * with the option to delete the image from storage.
 * <p>
 * This page is intended for admin users to view and manage individual images,
 * including profile pictures and event posters, directly from Firebase storage.
 */
public class AdminImagePage extends AppCompatActivity {
    ImageView imageView;
    String imageUriString, folderName;
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;

    Admin admin;
    /**
     * Initializes the activity, sets up the toolbar, and displays the selected image.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_image_activity);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.dashboard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView header = findViewById(R.id.mainHeader);
        header.setText("Images");


        imageView = findViewById(R.id.imageView);
        imageUriString = getIntent().getStringExtra("ImageUri");
        folderName = getIntent().getStringExtra("FolderName");

        // Display the image using Glide or Picasso
        ImageStorageManager storage = new ImageStorageManager(new Image(imageUriString, null), folderName);
        storage.displayImage(imageView);
        admin = new Admin();
        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageStorageManager storage = new ImageStorageManager(new Image(imageUriString, null), folderName);
                storage.deleteImage();
                Intent intent = new Intent(AdminImagePage.this, AdminViewImages.class);
                startActivity(intent);
            }
        });
    }
}
