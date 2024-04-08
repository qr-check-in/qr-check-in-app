package com.example.qrcheckin.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qrcheckin.Attendee.ProfileActivity;
import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.Common.MainActivity;
import com.example.qrcheckin.Event.CreateAddEventDetails;
import com.example.qrcheckin.Event.EventListView;
import com.example.qrcheckin.R;

public class AdminActivity extends AppCompatActivity {

        Button eventListButton;
        Button profileListButton;
        Admin admin;
    ImageView addAdminImageView;
    TextView addAdminTextView;
        ImageButton qrButton;
        ImageButton eventButton;
        ImageButton addEventButton;
        ImageButton profileButton;
        Button imageListButton;
        /**
         * Sets up the AdminActivity's user interface and event listeners.
         * This method initializes the activity, including toolbar setup, button initialization,
         * and configuration of onClickListeners for navigating to other activities within the app.
         *
         * @param savedInstanceState contains the most recent data if re-initialized after shutdown; otherwise null.
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.admin_main);
            addAdminImageView = findViewById(R.id.addAdminImageView);
            addAdminTextView = findViewById(R.id.addAdminTextView);
            ImageStorageManager storage = new ImageStorageManager("AddAdminQRCode.jpg");
            storage.displayImage(addAdminImageView);
            addAdminImageView.getLayoutParams().width = 400;
            addAdminImageView.getLayoutParams().height = 400;
            admin = new Admin();
            eventListButton = findViewById(R.id.btn_admin_event);
            profileListButton = findViewById(R.id.profile_admin_button);
            imageListButton = findViewById(R.id.image_admin_button);
            Toolbar toolbar = findViewById(R.id.dashboard);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            TextView header = findViewById(R.id.mainHeader);
            header.setText("DASHBOARD");
            qrButton = findViewById(R.id.qrButton);
            eventButton = findViewById(R.id.calenderButton);
            addEventButton = findViewById(R.id.addCalenderButton);
            profileButton = findViewById(R.id.profileButton);
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
        }
    }


