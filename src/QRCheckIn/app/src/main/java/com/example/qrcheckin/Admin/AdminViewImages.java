package com.example.qrcheckin.Admin;
import com.example.qrcheckin.Common.Image;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcheckin.Attendee.ProfileActivity;
import com.example.qrcheckin.Common.MainActivity;
import com.example.qrcheckin.Event.CreateAddEventDetails;
import com.example.qrcheckin.Event.EventListView;
import com.example.qrcheckin.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminViewImages extends AppCompatActivity implements ImageAdapter.OnImageClickListener {
    private RecyclerView imagesRecyclerView;
    private ImageAdapter adapter;
    private FirebaseFirestore db;
    private Map<Image, String> imageUriToFolderMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_images);

        Toolbar toolbar = findViewById(R.id.images);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageButton qrButton = findViewById(R.id.qrButton); // Make sure you have a correct ID here
        ImageButton eventButton = findViewById(R.id.calenderButton); // Make sure you have a correct ID here
        ImageButton addEventButton = findViewById(R.id.addCalenderButton); // This ID needs to be in your layout
        ImageButton profileButton = findViewById(R.id.profileButton);
        header.setText("Images");

        imagesRecyclerView = findViewById(R.id.image_recycler_view);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ImageAdapter(this, new HashMap<>()); // Use an empty map for initialization
        imagesRecyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        adapter.setOnImageClickListener(this);
        // Toolbar button listeners
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

        fetchAndDisplayImages();

    }

    private void fetchAndDisplayImages() {
        db.collection("Attendees").get()
                .addOnSuccessListener(attendeesSnapshots -> {
                    for (DocumentSnapshot attendeeSnapshot : attendeesSnapshots) {
                        if (attendeeSnapshot.contains("profile.profilePicture")) {
                            String profilePicUri = attendeeSnapshot.getString("profile.profilePicture.uriString");
                            if (profilePicUri != null) {
                                imageUriToFolderMap.put(new Image(profilePicUri, null), "/ProfilePictures"); // Assuming Attendee not required here
                            }
                        }
                    }
                    db.collection("events").get()
                            .addOnSuccessListener(eventsSnapshots -> {
                                for (DocumentSnapshot eventSnapshot : eventsSnapshots) {
                                    if (eventSnapshot.contains("poster")) {
                                        String posterUri = eventSnapshot.getString("poster.uriString");
                                        if (posterUri != null) {
                                            imageUriToFolderMap.put(new Image(posterUri, null), "/EventPosters"); // Again, assuming Attendee not needed
                                        }
                                    }
                                }
                                if (adapter == null) {
                                    adapter = new ImageAdapter(this, imageUriToFolderMap); // Ensure ImageAdapter is adapted for Image objects
                                    imagesRecyclerView.setAdapter(adapter);
                                } else {
                                    adapter.updateData(imageUriToFolderMap);
                                }
                            })
                            .addOnFailureListener(e -> Log.e("AdminViewImages", "Error fetching images: " + e));
                });
    }

    @Override
    public void onImageClick(Image image, int position) {
        String folderName = imageUriToFolderMap.get(image);
        Intent intent = new Intent(AdminViewImages.this, AdminImagePage.class);
        intent.putExtra("ImageUri", image.getUriString());
        intent.putExtra("FolderName", folderName);
        startActivity(intent);
    }
}