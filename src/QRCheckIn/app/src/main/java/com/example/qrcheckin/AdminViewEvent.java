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

public class AdminViewEvent extends AppCompatActivity {
    private RecyclerView eventsRecyclerView;
    private Admin admin;
    Button back;
    private AdminEventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_events_admin); // Ensure this is the correct layout file
        Toolbar toolbar = findViewById(R.id.events);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Current Events");
        back=findViewById(R.id.back_button);
        eventsRecyclerView = findViewById(R.id.event_recycler_view); // Ensure ID matches your XML
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        admin = new Admin();
        eventAdapter = new AdminEventAdapter(new ArrayList<>()); // Initialize adapter with an empty list
        eventsRecyclerView.setAdapter(eventAdapter);

        // Fetch and display events
        admin.browseEvents(new Admin.EventsCallback() {
            @Override
            public void onEventsFetched(List<Event> events) {
                eventAdapter.updateEvents(events); // Method in EventAdapter to update the events list
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(event);
            }
        });
    }
}
