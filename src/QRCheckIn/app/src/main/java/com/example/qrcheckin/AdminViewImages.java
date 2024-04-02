package com.example.qrcheckin;

import android.content.Intent;
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
import java.util.List;

public class AdminViewImages extends AppCompatActivity {
    private RecyclerView imagesRecyclerView;
    private ImageAdapter adapter;
    private FirebaseFirestore db;
    private List<Image> images = new ArrayList<>(); // Now storing Image objects
    Admin admin;
    Button back;

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
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        db = FirebaseFirestore.getInstance();

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
                                images.add(new Image(profilePicUri, null)); // Assuming Attendee not required here
                            }
                        }
                    }
                    db.collection("events").get()
                            .addOnSuccessListener(eventsSnapshots -> {
                                for (DocumentSnapshot eventSnapshot : eventsSnapshots) {
                                    if (eventSnapshot.contains("poster")) {
                                        String posterUri = eventSnapshot.getString("poster.uriString");
                                        if (posterUri != null) {
                                            images.add(new Image(posterUri, null)); // Again, assuming Attendee not needed
                                        }
                                    }
                                }
                                if (adapter == null) {
                                    adapter = new ImageAdapter(this, images); // Ensure ImageAdapter is adapted for Image objects
                                    imagesRecyclerView.setAdapter(adapter);
                                } else {
                                    adapter.updateData(images);
                                }
                            })
                            .addOnFailureListener(e -> Log.e("AdminViewImages", "Error fetching images: " + e));
                });
    }
}
