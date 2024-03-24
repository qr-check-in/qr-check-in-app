package com.example.qrcheckin;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdminActivity extends AppCompatActivity {

    Button eventListButton;
    Button profileListButton;
    Admin admin;
    Button imageListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);
        admin = new Admin();
        eventListButton = findViewById(R.id.btn_admin_event);
        profileListButton = findViewById(R.id.profile_admin_button);
        imageListButton = findViewById(R.id.image_admin_button);
        Toolbar toolbar = findViewById(R.id.dashboard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("DASHBOARD");
        eventListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), AdminViewEvent.class);
                startActivity(event);
            }
        });
        profileListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), AdminViewProfiles.class);
                startActivity(event);
            }
        });
        imageListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), AdminViewImages.class);
                startActivity(event);
            }
        });
    }
}

