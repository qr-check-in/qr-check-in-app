package com.example.qrcheckin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

    private EventDatabaseManager eventDb;


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
                Query checkinQuery = eventDb.getEventCollectionRef().whereEqualTo("checkInQRCode.hashedContent", scannedData);
                Query promoQuery = eventDb.getEventCollectionRef().whereEqualTo("promoQRCode.hashedContent", scannedData);

                // Query for check-in QR codes, check the attendee in if an event is found
                checkinQuery.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Retrieve the event of the scanned QR code
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();

                            // Check the attendee into the event, update the event's attendees accordingly
                            SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
                            String fcmToken = prefs.getString("token", "missing token");
                            AttendeeDatabaseManager attendeeDbManager = new AttendeeDatabaseManager(fcmToken);
                            EventDatabaseManager eventDbManager = new EventDatabaseManager(documentId);

                            attendeeDbManager.addEventID("attendedEvents", documentId); // Add event to attendee

                            String attendeeId = attendeeDbManager.getAttendeeDocRef().getId();
                            eventDbManager.addAttendeeID("attendee", attendeeId);       // Add attendee to event

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
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();

                            // Open the appropriate event page
                            Intent intent = new Intent(getApplicationContext(), EventPage.class);
                            intent.putExtra("DOCUMENT_ID", documentId);
                            startActivity(intent);
                        } else {
                            // No matching document found
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

}