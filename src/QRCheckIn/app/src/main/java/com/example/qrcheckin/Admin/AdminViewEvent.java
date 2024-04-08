
package com.example.qrcheckin.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcheckin.Attendee.ProfileActivity;
import com.example.qrcheckin.Common.LinearLayoutManagerWrapper;
import com.example.qrcheckin.Common.MainActivity;
import com.example.qrcheckin.Event.CreateAddEventDetails;
import com.example.qrcheckin.Event.Event;
import com.example.qrcheckin.Event.EventListView;
import com.example.qrcheckin.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
/**
 * Activity for admin users to view and manage current events.
 * This activity displays a list of events fetched from Firestore in a RecyclerView.
 * Admins can interact with events to view details or perform other actions.
 */
public class AdminViewEvent extends AppCompatActivity {
    private RecyclerView eventsRecyclerView;
    private FirebaseFirestore db;
    private AdminEventAdapter eventAdapter;
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    /**
     * Initializes the activity, sets up the toolbar and its actions, and prepares the RecyclerView for displaying events.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_events_admin);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.events);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Current Events");

        qrButton = findViewById(R.id.qrButton); // Make sure you have a correct ID here
        eventButton = findViewById(R.id.calenderButton); // Make sure you have a correct ID here
        addEventButton = findViewById(R.id.addCalenderButton); // This ID needs to be in your layout
        profileButton = findViewById(R.id.profileButton);
        db = FirebaseFirestore.getInstance();
        eventsRecyclerView = findViewById(R.id.event_recycler_view);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this));
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
        setUpRecyclerView();

    }

    /**
     * Configures the RecyclerView with a Firestore query for events, using FirestoreRecyclerAdapter for real-time updates.
     */
    private void setUpRecyclerView() {
        Query query = db.collection("events").orderBy("eventName", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .build();
        eventAdapter = new AdminEventAdapter(options);

        eventAdapter.setOnItemClickListener(new AdminEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();

                // Send the document id of the event to the Event Page before opening it
                Intent intent = new Intent(getApplicationContext(), AdminEventPage.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);
            }
        });
        eventsRecyclerView.setAdapter(eventAdapter);
    }

    /**
     * Lifecycle method to start listening to Firestore query updates when the activity is started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        eventAdapter.startListening();
    }

    /**
     * Lifecycle method to stop listening to Firestore query updates when the activity is stopped.
     */
    @Override
    protected void onStop() {
        super.onStop();
        eventAdapter.stopListening();
    }
}
