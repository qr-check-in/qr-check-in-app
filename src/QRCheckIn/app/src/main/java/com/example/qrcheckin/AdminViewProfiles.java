package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminViewProfiles extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private FirebaseFirestore db;
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
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
        header.setText("Current User Profiles");
        setupRecyclerView();
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        admin = new Admin();
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent event = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(event);
            }
        });
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

        fetchProfilesAndSetupAdapter();
    }
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        fetchProfilesAndSetupAdapter();
    }


    private void fetchProfilesAndSetupAdapter() {
        db.collection("Attendees").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Profile> profilesList = new ArrayList<>();
                documentIds.clear(); // Clear previous IDs
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> profileMap = (Map<String, Object>) document.get("profile");
                    if (profileMap != null) {
                        String name = (String) profileMap.get("name");
                        if (name != null) {
                            Profile profile = new Profile();
                            profile.setName(name);
                            profilesList.add(profile);
                            documentIds.add(document.getId()); // Store document ID
                        }
                    }
                }
                adapter = new ProfileAdapter(profilesList);
                adapter.setOnItemClickListener(position -> {
                    if (position >= 0 && position < documentIds.size()) {
                        String docId = documentIds.get(position);
                        // Intent to navigate to the ProfileDetailActivity with the document ID
                        Intent intent = new Intent(AdminViewProfiles.this, ProfileActivityAdmin.class);
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