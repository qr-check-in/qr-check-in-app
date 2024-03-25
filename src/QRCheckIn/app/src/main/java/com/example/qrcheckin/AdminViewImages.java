package com.example.qrcheckin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminViewImages extends AppCompatActivity {
    private RecyclerView imagesRecyclerView;
    private ImageAdapter imageAdapter;
    private List<String> imageUrls; // Populate this list with your image URLs
    Admin admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_images);
        Toolbar toolbar = findViewById(R.id.images);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Images");
        RecyclerView imagesRecyclerView = findViewById(R.id.image_recycler_view);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Set grid layout
        List<String> imageUris = new ArrayList<>();
        ImageAdapter imageAdapter = new ImageAdapter(this, imageUris);
        imagesRecyclerView.setAdapter(imageAdapter);

        Admin admin = new Admin(); // Assuming Admin contains browseImages()
        admin.browseImages(new Admin.ImagesCallback() {
            @Override
            public void onImagesFetched(List<String> imageUris) {
                // Update RecyclerView with fetched images
                imageAdapter.updateData(imageUris);
            }
        });
    }
}
