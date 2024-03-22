package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class QRCodeScan extends AppCompatActivity {
    TextView title;
    TextView location;
    TextView dateAndtime;
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    private boolean hasScanned = false;   // Boolean flag to track whether a scan has been performed
    String summary = null, destination = null, dateOfEvent = null, timeOfEvent = null, dtstart = null;

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
                Intent event = new Intent(getApplicationContext(), CreateNewEventScreen1.class);
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
        integrator.setBeepEnabled(true);
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

        if (!hasScanned) { // Only proceed if scanning hasn't been performed yet
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    // Separate the scanned data into different variables
                    String scannedData = result.getContents();

                    // Query Firestore to find the document with the matching hashedContent in the checkInQRCode field
                    Query query = eventDb.getCollectionRef().whereEqualTo("checkInQRCode.hashedContent", scannedData);

                    query.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // QR code scanned successfully, find the event it links to
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Retrieve the first matching document of the scanned qr code
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                String id = documentSnapshot.getId();

                                // Send the document id of the event to the Event Page before opening it
                                Intent intent = new Intent(getApplicationContext(), EventPage.class);
                                intent.putExtra("DOCUMENT_ID", id);
                                startActivity(intent);

                                hasScanned = true; // Set the flag to true after successful scan
                            } else {
                                // No matching document found
                                Toast.makeText(this, "No event found!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // An error occurred during the query execution, handle the error
                            Exception exception = task.getException(); // Retrieve the exception that occurred

                            if (exception instanceof FirebaseFirestoreException) {
                                FirebaseFirestoreException firestoreException = (FirebaseFirestoreException) exception;
                                FirebaseFirestoreException.Code errorCode = firestoreException.getCode(); // Retrieve the error code
                                // Handle specific error codes if needed, for example:
                                switch (errorCode) {
                                    case NOT_FOUND:
                                        // Handle document not found error
                                        break;
                                    default:
                                        // Handle other errors
                                        System.err.println("Firestore error occurred: " + exception.getMessage());
                                }
                            } else {
                                // Handle other types of exceptions
                                assert exception != null;
                                System.err.println("An error occurred: " + exception.getMessage());
                            }

                        }
                    });
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}