package com.example.qrcheckin.Event;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcheckin.Common.LinearLayoutManagerWrapper;
import com.example.qrcheckin.Common.MainActivity;
import com.example.qrcheckin.Attendee.ProfileActivity;
import com.example.qrcheckin.R;
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
    MaterialButton btnUpcomingTab;
    MaterialButton btnSignedUpTab;
    MaterialButton btnMyEventsTab;
    MaterialButton btnCurrentTab;
    String fcmToken;
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

        // Find main app tool bar buttons
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        recyclerView = findViewById(R.id.event_recycler_view);

        // Find event tab buttons
        btnUpcomingTab = findViewById(R.id.upcomingEventsButton);
        btnSignedUpTab = findViewById(R.id.signedUpVEventsButton);
        btnMyEventsTab = findViewById(R.id.myEventsButton);

        btnCurrentTab = btnUpcomingTab;
        btnCurrentTab.setPressed(true);
        eventButton.setPressed(true);       // https://stackoverflow.com/questions/9318331/keep-android-button-selected-state, 2024, Prompt: how  to keep a button selected

        Toolbar toolbar = findViewById(R.id.eventListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Ongoing Events");

        // Get user's fcm token (used for querying the right events in recycler view)
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        fcmToken = prefs.getString("token", "missing token");

        // Set up the recycler view of events to be displayed (displays all by default)
        Query query = eventDb.getCollectionRef()
                .orderBy("eventName", Query.Direction.DESCENDING);
        setUpRecyclerView(query);

        // Set up listeners for event tabs (signed-up, my events/organized by me)
        btnUpcomingTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the currently pressed tab
                pressButton(btnUpcomingTab);

                // Set up general query to return all events
                Query query = eventDb.getCollectionRef()
                        .orderBy("eventName", Query.Direction.DESCENDING);
                updateRecyclerView(query);
            }
        });
        btnSignedUpTab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Update the currently pressed tab
                pressButton(btnSignedUpTab);

                // Set up a query to return events this user signed up for
                Query query = eventDb.getCollectionRef()
                        .whereArrayContains("signups", fcmToken)
                        .orderBy("eventName", Query.Direction.DESCENDING);
                updateRecyclerView(query);
            }
        });
        btnMyEventsTab.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // Update the currently pressed tab
            pressButton(btnMyEventsTab);

            // Set up query to return events organized by this user
            Query query = eventDb.getCollectionRef()
                    .whereEqualTo("organizer", fcmToken)
                    .orderBy("eventName", Query.Direction.DESCENDING);
            updateRecyclerView(query);
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
                Intent intent = new Intent(getApplicationContext(), OrganizersEventPageActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);

            }
        });
    }

    /**
     * Sets up an EventAdapter on the recycler view and sends it the required query. Do not call
     *      this method more than once
     * @param query The query the recycler view will use to display events
     */
    private void setUpRecyclerView(Query query) {

        // Put the desired query into the adapter so it can use it to find the specified events
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .setLifecycleOwner(this)
                .build();

        eventAdapter = new EventAdapter(options);

        // Connect the recycler view to it's adapter and layout manager
        RecyclerView recyclerView = findViewById(R.id.event_recycler_view);
        recyclerView.setHasFixedSize(true);     // RecyclerView inside constraint layout, won't grow
        recyclerView.setItemAnimator(null);     // ItemAnimator is buggy, keep this OFF if possible
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this));
        recyclerView.setAdapter(eventAdapter);
    }

    /**
     * Modify the EventAdapter with a new query. Used to display different events in the recycler
     *      view.
     * @param newQuery The new query the recycler view will use
     */
    private void updateRecyclerView(Query newQuery) {
        eventAdapter.stopListening();
        FirestoreRecyclerOptions<Event> newOptions = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(newQuery, Event.class)
                .setLifecycleOwner(this)
                .build();
        eventAdapter.updateOptions(newOptions);
        eventAdapter.notifyDataSetChanged();
        eventAdapter.startListening();
    }

    /**
     * Called when this page is accessed by pressing the back button from another page.
     *      Re-selects the active tab and ensures updates to the event data set made while in
     *      another page are reflected here
     */
    @Override
    protected void onResume() {
        super.onResume();
        eventAdapter.notifyDataSetChanged();
        pressButton(btnCurrentTab);
    }

    /**
     * Change the colors of the tab buttons after being pressed to reflect the active page
     * @param button The new button to press
     */
    private void pressButton(MaterialButton button) {
        // Get colors
        ColorStateList pressedTint = ColorStateList.valueOf(getResources().
                getColor(R.color.Primary));
        ColorStateList notPressedTint = ColorStateList.valueOf(getResources().
                getColor(R.color.Secondary));

        // Deselect previous button
        btnCurrentTab.setPressed(false);
        btnCurrentTab.setBackgroundTintList(notPressedTint);

        // Select the new button
        btnCurrentTab = button;
        btnCurrentTab.setPressed(true);
        btnCurrentTab.setBackgroundTintList(pressedTint);
    }

}