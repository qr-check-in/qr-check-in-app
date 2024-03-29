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

public class AdminViewProfiles extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
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
        recyclerView = findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProfileAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        admin = new Admin();
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        adapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick( int position) {
//                String id = profileList.get(position);
//
//                // Send the document id of the event to the Event Page before opening it
//                Intent intent = new Intent(getApplicationContext(), EventPage.class);
//                intent.putExtra("DOCUMENT_ID", id);
//                startActivity(intent);
//
//            }
        //});
        fetchProfilesAndSetupAdapter();
    }


    private void fetchProfilesAndSetupAdapter() {
        admin.browseProfiles(profilesList -> {
            if (!profilesList.isEmpty()) {
                adapter.updateProfiles(profilesList); // Assuming you have this method in your adapter
            } else {
                // Handle the case where the profiles list is empty
            }
        });

    }




}