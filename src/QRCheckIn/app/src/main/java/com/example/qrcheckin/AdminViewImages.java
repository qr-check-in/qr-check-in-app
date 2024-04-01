package com.example.qrcheckin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminViewImages extends AppCompatActivity {
    private RecyclerView imagesRecyclerView;
    private ImageAdapter adapter;
    private FirebaseFirestore db;
    private List<String> imageUrls;
    private List<String> imageUris = new ArrayList<>(); // Store fetched image URLs
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

        // Initialize FirebaseFirestore instance before fetching images
        db = FirebaseFirestore.getInstance();

        // Now it's safe to fetch and display images
        fetchAndDisplayImages();

        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
    }

    private void fetchAndDisplayImages() {
        db.collection("Attendees").get()
                .addOnSuccessListener(attendeesSnapshots -> {
                    for (DocumentSnapshot attendeeSnapshot : attendeesSnapshots) {
                        if (attendeeSnapshot.contains("profile.profilePicture")) {
                            String profilePicUri = attendeeSnapshot.getString("profile.profilePicture.uriString");
                            if (profilePicUri != null) {
                                imageUris.add(profilePicUri);
                            }
                        }
                    }
                    db.collection("events").get()
                            .addOnSuccessListener(eventsSnapshots -> {
                                for (DocumentSnapshot eventSnapshot : eventsSnapshots) {
                                    if (eventSnapshot.contains("poster")) {
                                        String posterUri = eventSnapshot.getString("poster.uriString");
                                        if (posterUri != null) {
                                            imageUris.add(posterUri);
                                        }
                                    }
                                }
                                // Update adapter dataset
                                if (adapter == null) {
                                    adapter = new ImageAdapter(this, imageUris);
                                    imagesRecyclerView.setAdapter(adapter);
                                } else {
                                    adapter.updateData(imageUris);
                                }
                            })
                            .addOnFailureListener(e -> Log.e("AdminViewImages", "Error fetching event posters: " + e));
                })
                .addOnFailureListener(e -> Log.e("AdminViewImages", "Error fetching profile pictures: " + e));
    }


}
