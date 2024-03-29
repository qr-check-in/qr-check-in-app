package com.example.qrcheckin;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class QRCodeScan extends AppCompatActivity {
    TextView title;
    TextView location;
    TextView dateAndtime;
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    Boolean foundEvent = false;
    private EventDatabaseManager eventDb;

    // For location
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 100;
    String longitude = null, latitude = null;
    private String attendeeFcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);

        eventDb = new EventDatabaseManager();

        title = findViewById(R.id.topNavigationText);
        location = findViewById(R.id.location);
        dateAndtime = findViewById(R.id.dateAndtime);

        qrButton = findViewById(R.id.qrCode);
        eventButton = findViewById(R.id.events);
        addEventButton = findViewById(R.id.addEvent);
        profileButton = findViewById(R.id.profile);

        // initialize fusedLocationProviderClient for location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

//        // Get user's fcm token (used for querying the right events in recycler view)
//        prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
//        attendeeFcm = prefs.getString("token", "missing token");
//        Log.d("FCM TOKEN", "attendeeFcm : " + attendeeFcm);


        // uses the ZXing library to open the camera and proceed scanning
        startScanner();

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), QRCodeScan.class);
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
    }

    /**
     * using the IntentIntegrator class from the ZXing library to integrate barcode scanning functionality into your Android application
     * The ZXing library will return the scanned barcode data to your activity once the scanning is complete.
     */
    private void startScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QR code");
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }


    /**
     * In order to get data embedded in the QR code
     * @param resultCode An integer code that identifies the request
     * @param requestCode An integer result code returned by the child activity
     * @param data An Intent object that contains additional data returned by the child activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                // Query events to find out if the scanned QR code was a check-in or promo type
                String scannedData = result.getContents();
                Query checkinQuery = eventDb.getCollectionRef().whereEqualTo("checkInQRCode.hashedContent", scannedData);
                Query promoQuery = eventDb.getCollectionRef().whereEqualTo("promoQRCode.hashedContent", scannedData);

                // Query for check-in QR codes, check the attendee in if an event is found
                checkinQuery.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Retrieve the event of the scanned QR code
                            foundEvent = true;
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();

//                            Log.d("Document Id", "document id: " + documentId);

                            // Check the attendee into the event, update the event's attendees accordingly
                            SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
                            attendeeFcm = prefs.getString("token", "missing token");
                            AttendeeDatabaseManager attendeeDbManager = new AttendeeDatabaseManager(attendeeFcm);
                            EventDatabaseManager eventDbManager = new EventDatabaseManager(documentId);

                            attendeeDbManager.addToArrayField("attendedEvents", documentId); // Add event to attendee

                            String attendeeId = attendeeDbManager.getDocRef().getId();
                            eventDbManager.addToArrayField("attendee", attendeeId); // Add attendee to event

                            // Retrieve the value of the checkInStatus field
                            Boolean checkInStatus = documentSnapshot.getBoolean("checkInStatus");
                            Log.d("CheckInStatus", "checkInStatus: " + checkInStatus);



                            // Retrieve the boolean value of the "location" field under the "profile" field
//                            Boolean geoLocation = prefs.getBoolean("profile.trackGeolocation", false);
//                            Log.d("geoLocation", "geoLocation: " + geoLocation);


                            // if organizer wants the user location
//                            if (checkInStatus && geoLocation) {
                            if (checkInStatus) {
                                // call to get last location of user
                                getLastLocation();
                            }

                            // Open the appropriate event page
                            Intent intent = new Intent(getApplicationContext(), EventPage.class);
                            intent.putExtra("DOCUMENT_ID", documentId);
                            startActivity(intent);
                        }
                    } else {
                        Log.d("QueryError", "Error getting documents: ", task.getException());
                    }
                });

                // Query for promo QR codes
                promoQuery.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Retrieve the event of the scanned QR code
                            foundEvent = true;
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();

                            // Open the appropriate event page
                            Intent intent = new Intent(getApplicationContext(), EventPage.class);
                            intent.putExtra("DOCUMENT_ID", documentId);
                            startActivity(intent);
                        } else {
                            // No matching document found, return to main activity
                            if (!foundEvent) {
                                Toast.makeText(this, "No event found!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }

                    } else {
                        Log.d("QueryError", "Error getting documents: ", task.getException());
                    }
                });

            }
        } else {
            // QR code scanner wasn't the returning activity, so handle the return normally
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(QRCodeScan.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        Geocoder geocoder = new Geocoder(QRCodeScan.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
//                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                            String coordinates;
//                            latitude = String.valueOf(addresses.get(0).getLatitude());
//                            longitude = String.valueOf(addresses.get(0).getLongitude());

//                            coordinates = latitude + "," + longitude;
//
//                            Log.d("COORDINATES", "coordinates: " + coordinates);
//                            Log.d("attendeeFcm", "attendeeFcm: " + attendeeFcm);

                            // Convert latitude and longitude to GeoPoint
                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

                            // Get a reference to the Firestore document you want to update
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("Attendees").document(attendeeFcm);

                            // Update the document
                            docRef.update("location", geoPoint)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Field successfully updated
                                            // Handle success
                                            Log.d("getLastLocation", "onSuccess: " + geoPoint);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle failure
                                            Log.e("Firestore", "Error updating document", e);
                                        }
                                    });


                        } catch (Exception e) {
                            Log.e("getLastLocation", "Error getting last location", e);
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
        }, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

}