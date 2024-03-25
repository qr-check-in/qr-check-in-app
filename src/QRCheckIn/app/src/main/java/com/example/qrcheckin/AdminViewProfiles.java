package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button back;
    private List<Profile> profileList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_profiles);
        back=findViewById(R.id.back_button);
        Toolbar toolbar = findViewById(R.id.profiles);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        admin = new Admin();
        header.setText("Current User Profiles");
        recyclerView = findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(event);
            }
        });
        // Assume fetchProfiles() fills profileList with Profile objects fetched from Firestore
        admin.browseProfiles(new Admin.ProfilesCallback() {
            @Override
            public void onProfilesFetched(List<Map<String, Object>> profilesData) {
                List<Profile> tempProfileList = new ArrayList<>();
                for (Map<String, Object> profileData : profilesData) {
                    Profile profile = new Profile(); // Assuming default constructor is available
                    if (profileData.containsKey("name")) {
                        profile.setName((String) profileData.get("name"));
                    }
                    // Repeat for other fields
                    tempProfileList.add(profile);
                }

                // Now tempProfileList contains your profiles as List<Profile>
                runOnUiThread(() -> {
                    profileList.clear();
                    profileList.addAll(tempProfileList);
                    if(adapter == null) {
                        adapter = new ProfileAdapter(profileList, new ProfileAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Profile profile) {
                                Intent event = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(event);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged(); // Notify the adapter to refresh the UI
                    }
                });

            }
        });


    }

}
