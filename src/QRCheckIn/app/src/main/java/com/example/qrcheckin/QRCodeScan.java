package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class QRCodeScan extends AppCompatActivity{
    TextView title;
    TextView location;
    TextView dateAndtime;
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    private boolean hasScanned = false;   // Boolean flag to track whether a scan has been performed

    // Get access to the Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Assuming 'events' is the name of your collection
    CollectionReference eventsRef = db.collection("events");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);


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
                Intent event = new Intent(getApplicationContext(), ProfileFragment.class);
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
                else
                {
                    // Separate the scanned data into different variables
                    String scannedData = result.getContents();

                    final String[] summary = {null};
                    final String[] destination = { null };
                    final String[] dateOfEvent = { null };
                    final String[] timeOfEvent = { null };
                    final String[] dtstart = { null };


                    // Query Firestore to find the document with the matching hashedContent in the checkInQRCode field
                    Query query = eventsRef.whereEqualTo("checkInQRCode.hashedContent", scannedData);


                    query.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Check if any document matches the query
                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot != null && !querySnapshot.isEmpty())
                            {
                                // Retrieve the first matching document
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                                // Extract relevant information from the document
                                summary[0] = documentSnapshot.getString("eventName");
                                destination[0] = documentSnapshot.getString("eventLocation");
                                dateOfEvent[0] = documentSnapshot.getString("eventDate");
                                timeOfEvent[0] = documentSnapshot.getString("eventTime");

                                // make a string combined with date and time
                                dtstart[0] = dateOfEvent[0] + 'T' + timeOfEvent[0] + 'Z';


                            } else {
                                // No matching document found
                                Toast.makeText(this, "No event found!", Toast.LENGTH_SHORT).show();
                            }

                        } else
                        {
                            // An error occurred during the query execution, handle the error
                            Exception exception = task.getException(); // Retrieve the exception that occurred

                            if (exception instanceof FirebaseFirestoreException)
                            {
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



                    // get the date and time formatted
                    String formattedDateTime = null;
                    try {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.US);
                        Date date = inputFormat.parse(dtstart[0]);
                        formattedDateTime = outputFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Display or use the separated variables as needed
                    Toast.makeText(this, "CHECKED IN " + summary[0], Toast.LENGTH_SHORT).show();

                    // set the event details on the event page
                    title.setText(summary[0]);
                    location.setText(destination[0]);
                    dateAndtime.setText(formattedDateTime);

                    hasScanned = true; // Set the flag to true after successful scan
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}