package com.example.qrcheckin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        admin.browseProfiles(new Admin.ProfilesCallback() {
            @Override
            public void onProfilesFetched(List<Map<String, Object>> profiles) {
                List<Profile> profileObjects = new ArrayList<>();
                for (Map<String, Object> profileData : profiles) {
                    Profile profile = new Profile();
                    if (profileData.containsKey("name")) {
                        profile.setName((String) profileData.get("name"));
                    }
                    // Repeat for other fields you wish to set
                    profileObjects.add(profile);
                }
                // Now you have a List<Profile> populated with data
                // Proceed to update the RecyclerView's adapter with this list
                runOnUiThread(() -> {
                    profileList.clear();
                    profileList.addAll(profileObjects);
                    adapter.notifyDataSetChanged(); // Notify the adapter to refresh the UI
                });
            }
        });

        adapter = new ProfileAdapter(profileList);
        recyclerView.setAdapter(adapter);
    }
}
