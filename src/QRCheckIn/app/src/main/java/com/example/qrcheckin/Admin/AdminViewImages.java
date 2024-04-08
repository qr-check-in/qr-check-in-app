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
/**
 * Activity for displaying a grid of images fetched from Firestore,
 * including profile pictures and event posters.
 */
public class AdminViewImages extends AppCompatActivity implements ImageAdapter.OnImageClickListener {
    private RecyclerView imagesRecyclerView;
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    private ImageAdapter adapter;
    private FirebaseFirestore db;
    Map<Image, String> imageUriToFolderMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_images);

        Toolbar toolbar = findViewById(R.id.images);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        header.setText("Images");

        imagesRecyclerView = findViewById(R.id.image_recycler_view);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ImageAdapter(this, new HashMap<>()); // Use an empty map for initialization
        imagesRecyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        adapter.setOnImageClickListener(this);
        fetchAndDisplayImages();

    }
    /**
     * Fetches images from Firestore, including profile pictures of attendees
     * and event posters, then displays them in the RecyclerView.
     */
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

    /**
     * Handles clicks on images displayed in the RecyclerView.
     *
     * @param image    The Image object representing the clicked image.
     * @param position The position of the clicked image in the grid.
     */
    @Override
    public void onImageClick(Image image, int position) {
        String folderName = imageUriToFolderMap.get(image);
        Intent intent = new Intent(AdminViewImages.this, AdminImagePage.class);
        intent.putExtra("ImageUri", image.getUriString());
        intent.putExtra("FolderName", folderName);
        startActivity(intent);
    }
}