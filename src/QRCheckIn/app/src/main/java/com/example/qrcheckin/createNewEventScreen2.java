 package com.example.qrcheckin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

 public class createNewEventScreen2 extends AppCompatActivity {
     ImageButton qrButton;
     ImageButton eventButton;
     ImageButton addEventButton;
     ImageButton profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event_screen2);
    }
}