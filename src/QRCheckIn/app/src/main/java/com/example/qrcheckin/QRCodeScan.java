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

import com.google.firebase.firestore.DocumentSnapshot;
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
    private String fcmToken;

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

        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        fcmToken = prefs.getString("token", "missing token");


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
                            AttendeeDatabaseManager attendeeDbManager = new AttendeeDatabaseManager(fcmToken);
                            EventDatabaseManager eventDbManager = new EventDatabaseManager(documentId);

                            attendeeDbManager.addToArrayField("attendedEvents", documentId); // Add event to attendee
                            
                            String attendeeId = attendeeDbManager.getDocRef().getId();
                            eventDbManager.addToArrayField("attendee", attendeeId); // Add attendee to event

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

    public Boolean checkAddAdminQR(String scannedData){
        // The hashed content of the unique QR code to add an admin
        String hashedAddAdminContent = "7743f40037ce1ee22e53c5e88f79f3b2b3a690458344d482bae6ab82cba1dd0c";
        if(scannedData.equals(hashedAddAdminContent)){
            // Add this user as an admin instead of checking into an event
            AdminTokensDatabaseManager adminDb = new AdminTokensDatabaseManager(fcmToken);
            adminDb.storeAdminToken();
            // Return to MainActivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

}