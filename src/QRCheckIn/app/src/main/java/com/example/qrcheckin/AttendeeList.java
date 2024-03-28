package com.example.qrcheckin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class AttendeeList extends AppCompatActivity {

    Button getMap;
    String latitude, longitude;
    private static final int REQUEST_LOCATION = 101;
    RecyclerView recyclerView;
    //AttendeeAdapter attendeeAdapter;

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
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Total Participants: ");

        // Retrieve values passed by previous activity to determine if the event's signups list or attendee list should be displayed
        String documentId = getIntent().getStringExtra("EVENT_DOC_ID");
        String fieldName = getIntent().getStringExtra("FIELD_NAME");

        Log.d("ATTENDEE LIST", String.format("should display (%s) for (%s)", fieldName, documentId));

        // TODO: setup recycler view
        recyclerView = findViewById(R.id.attendee_recycler_view);
        setUpReyclerView(documentId, fieldName);

        getMap = findViewById(R.id.mapLocation);
        // Hide getMap button if we're viewing the signups
        if (Objects.equals(fieldName, "signupEvents")){
            getMap.setVisibility(View.INVISIBLE);
        }

        getMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });
    }

    /**
     * Creates query to get Attendees with this Event in their signup list.
     * Then sets up an AttendeeAdapter on the recycler view.
     * @param documentId String of the Event's document ID
     * @param fieldName String name of the array we want to query ("signupEvents" or attendedEvents")
     */
    private void setUpReyclerView(String documentId, String fieldName){
        AttendeeDatabaseManager attendeeDb = new AttendeeDatabaseManager();
        Query query = attendeeDb.getCollectionRef()
                .whereArrayContains(fieldName, documentId)
                .orderBy("profile.name", Query.Direction.DESCENDING);

        // Put the desired query into the adapter so it can use it to find the specified events
        FirestoreRecyclerOptions<Attendee> options = new FirestoreRecyclerOptions.Builder<Attendee>()
                .setQuery(query, Attendee.class)
                .setLifecycleOwner(this)
                .build();
        // needs AttendeeAdapter
        /*
        attendeeAdapter = new AttendeeAdapter(Options);
        // Connect the recycler view to it's adapter and layout manager
        RecyclerView recyclerView = findViewById(R.id.attendee_recycler_view);
        recyclerView.setHasFixedSize(true);     // RecyclerView inside constraint layout, won't grow
        recyclerView.setItemAnimator(null);     // ItemAnimator is buggy, keep this OFF if possible
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this));
        recyclerView.setAdapter(attendeeAdapter);
        */

    }

    /**
     * Called when this page is accessed by pressing the back button from another page.
     *      Ensures updates to the attendee data set made while in
     *      another page are reflected here
     */
    @Override
    protected void onResume() {
        super.onResume();
        //attendeeAdapter.notifyDataSetChanged();
    }


    private void getMyLocation() {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(AttendeeList.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            assert addresses != null;
                            latitude = String.valueOf(addresses.get(0).getLatitude());
                            longitude = String.valueOf(addresses.get(0).getLongitude());
                            Toast.makeText(AttendeeList.this, "latitude= "+latitude, Toast.LENGTH_SHORT).show();
                            Toast.makeText(AttendeeList.this, "longitude= "+longitude, Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
        else{
            askPermission();
        }
    }

    private void askPermission(){
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getMyLocation();
            }
            else{
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }
}




//package com.example.qrcheckin;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//
//public class AttendeeList extends AppCompatActivity implements LocationListener {
//
//    Button getMap;
//    LocationManager locationManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.attendee_list);
//
//        getMap = findViewById(R.id.mapLocation);
//
//        // Runtime Permissions
//        if (ContextCompat.checkSelfPermission(AttendeeList.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(AttendeeList.this, new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION
//            }, 100);
//        }
//
//        getMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLocation();
//            }
//        });
//    }
//
//    @SuppressLint("MissingPermission")
//    private void getLocation(){
//        try {
//            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, AttendeeList.this);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//        Toast.makeText(this, location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        LocationListener.super.onStatusChanged(provider, status, extras);
//    }
//
//    @Override
//    public void onProviderEnabled(@NonNull String provider) {
//        LocationListener.super.onProviderEnabled(provider);
//    }
//
//    @Override
//    public void onProviderDisabled(@NonNull String provider) {
//        LocationListener.super.onProviderDisabled(provider);
//    }
//}