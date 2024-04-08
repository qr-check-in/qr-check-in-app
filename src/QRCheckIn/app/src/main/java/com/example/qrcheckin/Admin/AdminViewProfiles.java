package com.example.qrcheckin.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcheckin.Attendee.Profile;
import com.example.qrcheckin.Attendee.Attendee;
import com.example.qrcheckin.Attendee.ProfileActivity;
import com.example.qrcheckin.Attendee.ProfilePicture;
import com.example.qrcheckin.Common.MainActivity;
import com.example.qrcheckin.Event.CreateAddEventDetails;
import com.example.qrcheckin.Event.EventListView;
import com.example.qrcheckin.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * AdminViewProfiles is an activity class that displays a list of user profiles fetched from Firestore.
 * It uses a RecyclerView to list profiles, allowing admins to view and potentially manage these profiles.
 */
public class AdminViewProfiles extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminProfileAdapter adapter;
    private FirebaseFirestore db;
    private List<String> documentIds = new ArrayList<>();

    Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_profiles);
        Toolbar toolbar = findViewById(R.id.profiles);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        header.setText("Current User Profiles");
        setupRecyclerView();

        admin = new Admin();

        fetchProfilesAndSetupAdapter();
    }
    /**
     * Initializes the RecyclerView used to display the profiles.
     */
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        fetchProfilesAndSetupAdapter();
    }

    /**
     * Fetches profiles from Firestore and sets up the RecyclerView adapter with the fetched data.
     * This method retrieves each profile document, constructs Profile objects from them,
     * and updates the adapter for display.
     */
    private void fetchProfilesAndSetupAdapter() {
        db.collection("Attendees").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Profile> profilesList = new ArrayList<>();
                documentIds.clear(); // Clear previous IDs
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> profileMap = (Map<String, Object>) document.get("profile");
//                    Map<String, Object> profilePictureMap = (Map<String, Object>) document.get("profilePicture");
                    String profilePicUrl = (String) document.get("profile.profilePicture.uriString");
                    if (profileMap != null && profilePicUrl == null) {
                        String name = (String) profileMap.get("name");
                        if (name != null) {
                            Profile profile = new Profile();
                            profile.setName(name);
                            profilesList.add(profile);
                            documentIds.add(document.getId()); // Store document ID
                        }
                    }
                    else if (profileMap != null && profilePicUrl != null) {
                        String name = (String) profileMap.get("name");
                        if (name != null) {
                            Profile profile = new Profile();
                            Attendee attendee = new Attendee();
                            ProfilePicture pic = new ProfilePicture(profilePicUrl, attendee);
                            profile.setName(name);
                            profile.setProfilePicture(pic);
                            profilesList.add(profile);
                            documentIds.add(document.getId()) ; // Store document ID
                        }
                    }
                }
                adapter = new AdminProfileAdapter(profilesList);
                adapter.setOnItemClickListener(position -> {
                    if (position >= 0 && position < documentIds.size()) {
                        String docId = documentIds.get(position);
                        // Intent to navigate to the ProfileActivityAdmin with the document ID
                        Intent intent = new Intent(AdminViewProfiles.this, AdminProfileActivity.class);
                        intent.putExtra("DOCUMENT_ID", docId);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
            } else {
                Log.w("AdminViewProfiles", "Error getting documents.", task.getException());
            }
        });
    }




}