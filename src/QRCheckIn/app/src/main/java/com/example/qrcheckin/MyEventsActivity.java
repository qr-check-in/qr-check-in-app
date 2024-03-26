package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class MyEventsActivity extends AppCompatActivity {
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    RecyclerView recyclerView;
    MaterialButton upcomingEvents;
    MaterialButton signedUpEvents;
    MaterialButton myEvents;
    private EventAdapter eventAdapter;
    private EventDatabaseManager eventDb;

    /**
     * Sets up RecyclerView with an adapter & configures the toolbar.
     * Sets listeners for toolbar buttons to navigate to different parts of the app.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                            contains data it most recently supplied.
     *                           Otherwise, its null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        eventDb = new EventDatabaseManager();

        setUpRecyclerView();

        // Set up tool bar
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        recyclerView = findViewById(R.id.event_recycler_view);

        // tabbar
        upcomingEvents = findViewById(R.id.upcomingEventsButton);
        signedUpEvents = findViewById(R.id.signedUpVEventsButton);
        myEvents = findViewById(R.id.myEventsButton);


        eventButton.setPressed(true);       // https://stackoverflow.com/questions/9318331/keep-android-button-selected-state, 2024, Prompt: how  to keep a button selected

        Toolbar toolbar = findViewById(R.id.eventListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Ongoing Events");

        myEvents.setPressed(true);
        // Listner for signed up events button
        signedUpEvents.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), SignedUpEvents.class);
                startActivity(event);
            }
        });

        // Listner for upcoming button
        upcomingEvents.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), EventListView.class);
                startActivity(event);
            }
        });

        // Set listener for "Scan QR code" toolbar button
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(event);
            }
        });

        // Set listener for "Add event" toolbar button
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), CreateAddEventDetails.class);
                startActivity(event);
            }
        });

        // Set listener for "Profile" toolbar button
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(event);

            }
        });

        // If an event is clicked, open its event page
        eventAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();

                // Send the document id of the event to the Event Page before opening it
                Intent intent = new Intent(getApplicationContext(), EventPage.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);

            }
        });
    }

    /**
     * Sets up an EventAdapter on the recycler view and sends it the required query
     */
    private void setUpRecyclerView() {
        // Set up a general query that returns "event" items from the database
        Query query = eventDb.getCollectionRef().orderBy("eventName", Query.Direction.DESCENDING);

        // Put this query into the adapter so it can use it
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .setLifecycleOwner(this)
                .build();

        eventAdapter = new EventAdapter(options);

        // Connect the recycler view to it's adapter and layout manager
        RecyclerView recyclerView = findViewById(R.id.event_recycler_view);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }
}

