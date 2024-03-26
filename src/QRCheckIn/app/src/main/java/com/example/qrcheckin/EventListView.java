package com.example.qrcheckin;

import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 * An AppCompatActivity that displays a list view of events fetched from Firestore database.
 * Provides navigation buttons in the toolbar.
 */
public class EventListView extends AppCompatActivity {
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    RecyclerView recyclerView;
    MaterialButton upcomingEvents;
    MaterialButton signedUpEvents;
    MaterialButton myEvents;
    String fcmToken;
    MaterialButton BtnPressedTabButton;
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
        setContentView(R.layout.activity_event_list_view);
        eventDb = new EventDatabaseManager();

        // Get user's fcm token (used for querying the right events in recycler view)
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        fcmToken = prefs.getString("token", "missing token");

        // Set up the recycler view of events to be displayed (displays all by default)
        Query query = eventDb.getCollectionRef()
                .orderBy("eventName", Query.Direction.DESCENDING);
        setUpRecyclerView(query);

        // Find main app tool bar buttons
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        recyclerView = findViewById(R.id.event_recycler_view);

        // Find event tab buttons
        upcomingEvents = findViewById(R.id.upcomingEventsButton);
        signedUpEvents = findViewById(R.id.signedUpVEventsButton);
        myEvents = findViewById(R.id.myEventsButton);

        BtnPressedTabButton = upcomingEvents;
        BtnPressedTabButton.setPressed(true);
        eventButton.setPressed(true);       // https://stackoverflow.com/questions/9318331/keep-android-button-selected-state, 2024, Prompt: how  to keep a button selected

        Toolbar toolbar = findViewById(R.id.eventListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Ongoing Events");

        // Set up listeners for event tabs (signed-up, my events/organized by me)
        upcomingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the currently pressed button
                BtnPressedTabButton.setPressed(false);
                BtnPressedTabButton = upcomingEvents;
                BtnPressedTabButton.setPressed(true);

                // Set up general query to return all events
                Query query = eventDb.getCollectionRef()
                        .orderBy("eventName", Query.Direction.DESCENDING);
                setUpRecyclerView(query);
            }
        });
        signedUpEvents.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Update the currently pressed button
                BtnPressedTabButton.setPressed(false);
                BtnPressedTabButton = signedUpEvents;
                BtnPressedTabButton.setPressed(true);

                // Set up a query to return events this user signed up for
                Query query = eventDb.getCollectionRef()
                        .whereArrayContains("signups", fcmToken)
                        .orderBy("eventName", Query.Direction.DESCENDING);
                setUpRecyclerView(query);
            }
        });
        myEvents.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // Update the currently pressed button
            BtnPressedTabButton.setPressed(false);
            BtnPressedTabButton = myEvents;
            BtnPressedTabButton.setPressed(true);

            // Set up query to return events organized by this user
            Query query = eventDb.getCollectionRef()
                    .whereEqualTo("organizer", fcmToken)
                    .orderBy("eventName", Query.Direction.DESCENDING);
            setUpRecyclerView(query);
        }
        });

        // Set up listeners for main app toolbar buttons
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(event);
            }
        });
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), CreateAddEventDetails.class);
                startActivity(event);
            }
        });
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
    private void setUpRecyclerView(Query displayedItems) {

        // Put the desired query into the adapter so it can use it to find the specified events
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(displayedItems, Event.class)
                .setLifecycleOwner(this)
                .build();

        eventAdapter = new EventAdapter(options);

        // Connect the recycler view to it's adapter and layout manager
        RecyclerView recyclerView = findViewById(R.id.event_recycler_view);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }

    private void pressButton(MaterialButton button) {
        
    }

}