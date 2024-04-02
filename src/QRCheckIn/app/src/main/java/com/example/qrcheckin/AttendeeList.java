package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;


public class AttendeeList extends AppCompatActivity {

    Button getMap;
    private String documentId;
    private String fieldName;
    ArrayList<String> attendeeList = new ArrayList<>(); // List to hold attendee names
    final ArrayList<LatLng> attendeeListGeoPoints = new ArrayList<>(); // List hold all attendee's Coordinates
    boolean booleanCheckInStatus = false;
    FirebaseFirestore db;
    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_list);

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

        // TODO: setup recycler view

        getMap = findViewById(R.id.mapLocation);

        // get all the attendees in the event from Firestore
        getAllAttendees();

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
                        getMap.setVisibility(View.INVISIBLE);
                    }

                    // Check if attendees list is  null
                    if (attendeeList == null) {
                        Log.d("AttendeeList", "No attendees found");
                    } else {
                        // If attendees list is not null, proceed to get geo points
                        getAllGeoPoints();
                        header.setText("Total Participants: " + attendeeList.size());
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