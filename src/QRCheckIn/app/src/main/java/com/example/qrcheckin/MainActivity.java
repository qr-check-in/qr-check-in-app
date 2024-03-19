package com.example.qrcheckin;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Entry point of the app, hosts main interface.
 * Provides buttons for; scanning QR codes, viewing event list, adding a new event, accessing the user profile.
 * It initializes fetching and storing the FCM token.
 */
public class MainActivity extends AppCompatActivity{
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    private String fcmToken;
    Button scanButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference eventsRef = db.collection("events");

    /**
     * Sets up UI and initializes application settings.
     * Sets up toolbar buttons & one-time setup of fetching the FCM token.
     *
     * @param savedInstanceState If activity is re-initialized after previously being shut down, contains
     *                          most recent data.
     *                          Otherwise, it is null.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qrButton = findViewById(R.id.qrButton);
        qrButton.setPressed(true);

        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        scanButton = findViewById(R.id.scanButton);

        // Creates a sub applicaton. If app.hasCheckFcmToken is false, it means the app has just been opened
        OpenApp app = (OpenApp) this.getApplicationContext();
        if (!app.hasCheckedFcmToken){
            // here we can call any methods we only want to occur once upon opening the app
            Database db = new Database();

            // Get and store this app installation's fcm token string
            // https://stackoverflow.com/questions/51834864/how-to-save-a-fcm-token-in-android , 2018, Whats Going On
            SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
            SharedPreferences.Editor editor = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE).edit();
            db.getFcmToken(editor);
            fcmToken = prefs.getString("token", "missing token");

            // Check if an Attendee object associated with this fcm token already exists
            db.checkExistingAttendees(fcmToken);
            Log.d("Firestore", String.format("fcmToken STRING (%s) stored", fcmToken));
            app.hasCheckedFcmToken = true;
        }

//        Set the Header of the App
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("QRCheckIN");


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanner();
            }
        });

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
            // If activity that launched was a qr code scan, parse and handle the scan
            if (result.getContents() == null) {
                // If user pressed the back button
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // Query Firestore to find the document with the matching hashedContent in the checkInQRCode field
                String scannedData = result.getContents();
                Query query = eventsRef.whereEqualTo("checkInQRCode.hashedContent", scannedData);
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Query successful, find the event it links to
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Retrieve the first matching document of the scanned qr code
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            String id = documentSnapshot.getId();

                            // Send the document id of the event to the Event Page before opening it
                            Intent intent = new Intent(getApplicationContext(), EventPage.class);
                            intent.putExtra("DOCUMENT_ID", id);
                            startActivity(intent);
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
            // Activity that is returning is not a QR code scan, let the super method deal with it
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}