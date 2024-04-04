package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
