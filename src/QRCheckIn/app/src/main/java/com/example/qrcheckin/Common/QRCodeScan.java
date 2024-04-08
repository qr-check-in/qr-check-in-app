package com.example.qrcheckin.Common;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.example.qrcheckin.Admin.AdminTokensDatabaseManager;
import com.example.qrcheckin.Attendee.AttendeeDatabaseManager;
import com.example.qrcheckin.Attendee.ProfileActivity;
import com.example.qrcheckin.Event.CreateAddEventDetails;
import com.example.qrcheckin.Event.EventDatabaseManager;
import com.example.qrcheckin.Event.EventListView;
import com.example.qrcheckin.Event.OrganizersEventPageActivity;
import com.example.qrcheckin.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


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
    SharedPreferences prefs;
    private AttendeeDatabaseManager dbManager;
    private FirebaseFirestore db;
    Boolean trackGeolocation = false;


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

        // Get user's fcm token (used for querying the right events in recycler view)
        prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        attendeeFcm = prefs.getString("token", "missing token");
        dbManager = new AttendeeDatabaseManager(attendeeFcm);

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
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QR code");
        integrator.setBeepEnabled(false);
        // https://stackoverflow.com/questions/34983201/change-qr-scanner-orientation-with-zxing-in-android-studio, 2024, how to change the orientation of teh camera
        integrator.setCaptureActivity(CaptureActivityProtrait.class);
        integrator.initiateScan();

        // get user geolocation
        getLastLocation();
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
                if(checkAddAdminQR(scannedData)){
                    // QR code to add an admin was scanned, don't look for a matching event
                    finish();
                    return;
                }
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

                            // Check the attendee into the event, update the event's attendees accordingly
                            AttendeeDatabaseManager attendeeDbManager = new AttendeeDatabaseManager(attendeeFcm);
                            EventDatabaseManager eventDbManager = new EventDatabaseManager(documentId);

                            attendeeDbManager.incrementCheckInCount("attendedEvents", documentId); // Add event to attendee
                            String attendeeId = attendeeDbManager.getDocRef().getId();
                            eventDbManager.addToArrayField("attendee", attendeeId); // Add attendee to event
                            Toast.makeText(getApplicationContext(), "Successfully Checked-into Event", Toast.LENGTH_LONG).show();
                            // Open the appropriate event page
                            Intent intent = new Intent(getApplicationContext(), OrganizersEventPageActivity.class);
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
                            Intent intent = new Intent(getApplicationContext(), OrganizersEventPageActivity.class);
                            intent.putExtra("DOCUMENT_ID", documentId);
                            startActivity(intent);
                        } else {
                            // No matching document found, return to main activity
                            if (!foundEvent) {
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
            // Initialize Firestore
            db = FirebaseFirestore.getInstance();

            // Get the document reference for the user's profile
            DocumentReference docRef = db.collection("Attendees").document(attendeeFcm);

            // Fetch the document
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the value of the trackGeolocation field
                        trackGeolocation = document.getBoolean("profile.trackGeolocation");
                        if (trackGeolocation != null && trackGeolocation) {
                            // If geolocation tracking is enabled, get the last location
                            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        try {
                                            // Convert latitude and longitude to GeoPoint
                                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

                                            // updates the Attendee's GeoPoint location in firestore
                                            dbManager.updateAttendeeLocation("location", geoPoint);

                                        } catch (Exception e) {
                                            Log.e("getLastLocation", "Error getting last location", e);
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        Log.d("getLastLocation", "No such document");
                        // Handle case where document does not exist
                    }
                } else {
                    Log.d("getLastLocation", "get failed with ", task.getException());
                    // Handle failure case
                }
            });
        } else {
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
                // updates the Attendee's geolocation attribute
                dbManager.updateAttendeeBoolean("profile.trackGeolocation",true);
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Boolean checkAddAdminQR(String scannedData){
        // The hashed content of the unique QR code to add an admin
        String hashedAddAdminContent = "7743f40037ce1ee22e53c5e88f79f3b2b3a690458344d482bae6ab82cba1dd0c";
        if(scannedData.equals(hashedAddAdminContent)){
            // Add this user as an admin instead of checking into an event
            AdminTokensDatabaseManager adminDb = new AdminTokensDatabaseManager(attendeeFcm);
            adminDb.storeAdminToken();
            // Return to MainActivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}