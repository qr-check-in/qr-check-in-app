package com.example.qrcheckin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class AdminImagePage extends AppCompatActivity {
    ImageView imageView;
    String imageUriString, folderName;
    Admin admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_image_activity);
        Toolbar toolbar = findViewById(R.id.dashboard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Images");
        imageView = findViewById(R.id.imageView);
        imageUriString = getIntent().getStringExtra("ImageUri");
        folderName = getIntent().getStringExtra("FolderName");

        // Display the image using Glide or Picasso
        ImageStorageManager storage = new ImageStorageManager(new Image(imageUriString, null), folderName);
        storage.displayImage(imageView);

        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(view -> finish());
    }
}
