package com.example.qrcheckin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AdminViewProfiles extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private Admin admin;
    private List<Profile> profileList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_profiles);
        Toolbar toolbar = findViewById(R.id.profiles);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        admin = new Admin();
        header.setText("Current User Profiles");
        recyclerView = findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Assume fetchProfiles() fills profileList with Profile objects fetched from Firestore
        admin.browseProfiles();

        adapter = new ProfileAdapter(profileList);
        recyclerView.setAdapter(adapter);
    }
}
