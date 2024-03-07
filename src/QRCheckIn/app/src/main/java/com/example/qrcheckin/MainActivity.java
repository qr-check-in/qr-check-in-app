package com.example.qrcheckin;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity{
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    private String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qrButton = findViewById(R.id.qrButton);
        qrButton.setPressed(true);

        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);

        // Creates a sub applicaton. If app.hasCheckFcmToken is false, it means the app has just been opened
        OpenApp app = (OpenApp) this.getApplicationContext();
        if (!app.hasCheckedFcmToken){
            // here we can call any methods we only want to occur once upon opening the app
            Database db = new Database();

            // Get and store this app installation's fcm token string
            // https://stackoverflow.com/questions/51834864/how-to-save-a-fcm-token-in-android , 2018, Whats Going On
            SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
            SharedPreferences.Editor editor = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE).edit();
            db.getFcmToken(editor);
            fcmToken = prefs.getString("token", "missing token");

            // Check if an Attendee object associated with this fcm token already exists
            db.checkExistingAttendees(fcmToken);
            Log.d("Firestore", String.format("TESTTOKEN STRING (%s) stored", fcmToken));
            app.hasCheckedFcmToken = true;
        }

//        Set the Header of the App
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("QRCheckIN");

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), EventListView.class);
                startActivity(event);
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), createNewEventScreen1.class);
                startActivity(event);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), profileFragment.class);
                startActivity(event);

            }
        });

    }
}