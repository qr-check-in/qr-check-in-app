package com.example.qrcheckin;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    ImageButton eventListButton;
    ImageButton profileListButton;
    ImageButton imageListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        eventListButton = findViewById(R.id.btnEvent);
        profileListButton = findViewById(R.id.profile_button);
        imageListButton = findViewById(R.id.image_button);

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

