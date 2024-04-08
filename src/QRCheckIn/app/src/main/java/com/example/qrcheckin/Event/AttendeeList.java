package com.example.qrcheckin.Event;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcheckin.Attendee.Attendee;
import com.example.qrcheckin.Attendee.AttendeeAdapter;
import com.example.qrcheckin.Attendee.AttendeeDatabaseManager;
import com.example.qrcheckin.Common.LinearLayoutManagerWrapper;
import com.example.qrcheckin.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

public class AttendeeList extends AppCompatActivity {

    Button getMap;
    private String fieldName;
    RecyclerView recyclerView;
    AttendeeAdapter attendeeAdapter;
    AttendeeDatabaseManager attendeeDb;
    private static final int REQUEST_LOCATION = 101;
    private EventDatabaseManager eventDb;
    private String fcmToken;
    private String documentId;
    ArrayList<String> attendeeList = new ArrayList<>(); // List to hold attendee names
    final ArrayList<LatLng> attendeeListGeoPoints = new ArrayList<>(); // List hold all attendee's Coordinates
    boolean booleanCheckInStatus = false;
    FirebaseFirestore db;
    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_list);

        // Set up recycler view of attendees
        getMap = findViewById(R.id.mapLocation);

        // Manage Toolbar
        Toolbar toolbar = findViewById(R.id.attendee_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        header = findViewById(R.id.mainHeader);

        // Retrieve values passed by previous activity to determine if the event's signups list or attendee list should be displayed
        documentId = getIntent().getStringExtra("EVENT_DOC_ID");
        fieldName = getIntent().getStringExtra("FIELD_NAME");

        // Set up the recycler view of attendees to be displayed
        attendeeDb = new AttendeeDatabaseManager();
        assert documentId != null;
        assert fieldName != null;
        Query query;
        if (fieldName.equals("signupEvents")) {
            query = attendeeDb.getCollectionRef()
                    .whereArrayContains(fieldName, documentId);
            // Hide getMap button if we're viewing the signups
            getMap.setVisibility(View.GONE);
            setUpRecyclerView(query, fieldName, documentId);
            // get all the attendees in the event from Firestore
            getAllAttendees();

        } else {
            // Set the search query
            String searchString = String.format("%s.%s", fieldName, documentId);
            query = attendeeDb.getCollectionRef()
                    .whereGreaterThan(searchString, 0);
            setUpRecyclerView(query, fieldName, documentId);

            // get all the attendees in the event from Firestore
            getAllAttendees();

            // Set the map button listener
            getMap.setVisibility(View.VISIBLE);
            getMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!attendeeListGeoPoints.isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("AllGeoPoints", attendeeListGeoPoints);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AttendeeList.this, "No geolocation available!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    /**
     * Sets up an AttendeeAdapter on the recycler view and sends it the required
     * query. Do not call
     * this method more than once
     * 
     * @param query         The query the recycler view will use to display events
     * @param fieldName     String from options: ["signupEvents", "attendedEvents"] to
     *                          display
     * @param documentId    The String document id of the event to get the attendee list for
     */
    private void setUpRecyclerView(Query query, String fieldName, String documentId) {
        // Put the desired query into the adapter so it can use it to find the specified
        // events
        FirestoreRecyclerOptions<Attendee> options = new FirestoreRecyclerOptions.Builder<Attendee>()
                .setQuery(query, Attendee.class)
                .setLifecycleOwner(this)
                .build();

        // Returns true if the adapter should display attendees who have checked-in/attended the
        // event, return false for attendees who have signed up for the event
        attendeeAdapter = new AttendeeAdapter(options, documentId, position -> {
            if (Objects.equals(fieldName, "attendedEvents")) {
                return true;
            } else if (Objects.equals(fieldName, "signupEvents")) {
                return false;
            } else {
                return true;
            }
        });

        // Connect the recycler view to it's adapter and layout manager
        recyclerView = findViewById(R.id.recycler_view_attendee_check_ins);
        recyclerView.setHasFixedSize(true); // RecyclerView inside constraint layout, won't grow
        recyclerView.setItemAnimator(null); // ItemAnimator is buggy, keep this OFF if possible
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this));
        recyclerView.setAdapter(attendeeAdapter);
    }

    // openai, 2024, chatgpt: how to pass the document Id through the upButton

    /**
     * Pass information through the the up button
     * 
     * @param item The menu item that was selected.
     *
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate back to OrganizersEventPageActivity with documentId
                Intent intent = new Intent(getApplicationContext(), OrganizersEventPageActivity.class);
                intent.putExtra("DOCUMENT_ID", documentId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getAllAttendees() {
        // Get a reference to the Firestore database
        db = FirebaseFirestore.getInstance();

        // Get the document reference using the document ID
        DocumentReference docRef = db.collection("events").document(documentId);

        // Fetch the document
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Retrieve the list of attendees from the document
                    attendeeList = (ArrayList<String>) documentSnapshot.get("attendee");

                    // get the checkInStatus from event
                    booleanCheckInStatus = documentSnapshot.getBoolean("checkInStatus");

                    if (booleanCheckInStatus) {
                        // If CheckInStatus of event is true, show the getMap button
                        getMap.setVisibility(View.VISIBLE);
                    } else {
                        // If CheckInStatus of event is false, hide the getMap button
                        getMap.setVisibility(View.GONE);
                    }

                    // Check if attendees list is  null
                    if (attendeeList == null) {
                        Log.d("AttendeeList", "No attendees found");
                    } else {
                        // If attendees list is not null, proceed to get geo points
                        getAllGeoPoints();
                        int size = 0;
                        size = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
                        header.setText("All Attendees");
                    }
                } else {
                    Log.d("AttendeeList", "Document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("AttendeeList", "Error getting attendees", e);
            }
        });
    }

    private void getAllGeoPoints() {
        db = FirebaseFirestore.getInstance();

        // Loop through each attendee in the attendeeList
        for (String attendeeId : attendeeList) {
            DocumentReference attendeeRef = db.collection("Attendees").document(attendeeId);

            // Fetch the document for each attendee
            attendeeRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        // Retrieve the location field (assuming it's a GeoPoint) from the document
                        GeoPoint geoPoint = documentSnapshot.getGeoPoint("location");

                        if (geoPoint != null) {
                            // Create a LatLng object using the GeoPoint's latitude and longitude values
                            LatLng location = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

                            // Add the LatLng to the attendeeListGeoPoints
                            attendeeListGeoPoints.add(location);
                        }

                    } else {
                        Log.d("AttendeeList", "Attendee document does not exist for ID: " + attendeeId);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("AttendeeList", "Error getting attendee document for ID: " + attendeeId, e);
                }
            });
        }
        Log.d("AllGeoPoints", "getAllGeoPoints: " + attendeeListGeoPoints);
    }
}