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


public class AdminImagePage extends AppCompatActivity {
    private String imageUriString, folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_image_activity);
        Toolbar toolbar = findViewById(R.id.dashboard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Images");
        ImageView imageView = findViewById(R.id.imageView);
        imageUriString = getIntent().getStringExtra("ImageUri");
        folderName = getIntent().getStringExtra("FolderName");
        ImageButton qrButton = findViewById(R.id.qrButton);
        ImageButton eventButton = findViewById(R.id.calenderButton);
        ImageButton addEventButton = findViewById(R.id.addCalenderButton);
        ImageButton profileButton = findViewById(R.id.profileButton);
        qrButton.setOnClickListener(v -> {
            Intent event = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(event);
        });

        // Set the "Add event" toolbar button listener
        addEventButton.setOnClickListener(v -> {
            Intent event = new Intent(getApplicationContext(), CreateAddEventDetails.class);
            startActivity(event);
        });

        // Set the "Profile" toolbar button listener
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(event);

            }
        });
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), EventListView.class);
                startActivity(event);
            }
        });
        // Display the image using Glide or Picasso
        ImageStorageManager storage = new ImageStorageManager(new Image(imageUriString, null), folderName);
        storage.displayImage(imageView);
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
