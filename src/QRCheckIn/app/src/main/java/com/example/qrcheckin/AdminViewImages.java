package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminViewImages extends AppCompatActivity {
    private RecyclerView imagesRecyclerView;
    private ImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>(); // Store fetched image URLs
    Admin admin; // Ensure you have an Admin instance that includes the browseImages method
    Button back;
    private interface ImageFetchCallback {
        void onImageUrlsFetched(List<String> urls);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_images);

        Toolbar toolbar = findViewById(R.id.images);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Images");

        imagesRecyclerView = findViewById(R.id.image_recycler_view);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Adjust the number of columns as needed

        admin = new Admin();
        fetchAndDisplayImages();

        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
    }

    private void fetchAndDisplayImages() {
        Admin admin = new Admin();
        admin.browseImages(imageUris -> {
            // Assuming you have an imageUrls list and an imageAdapter as before
            imageUrls.clear();
            imageUrls.addAll(imageUris);
            if (imageAdapter == null) {
                imageAdapter = new ImageAdapter(this, imageUrls);
                imagesRecyclerView.setAdapter(imageAdapter);
            } else {
                imageAdapter.notifyDataSetChanged();
            }
        });
    }
}
