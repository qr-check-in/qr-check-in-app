package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcheckin.R;
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
                    String[] lines = scannedData.split("\n");
                    String summary = null, destination = null, dtstart = null;

                    // Retrieve the Title, description, location, time and date form QR code data
                    for (String line : lines) {
                        if (line.startsWith("TITLE:")) {
                            summary = line.substring("TITLE:".length()).trim();
                        } else if (line.startsWith("LOCATION:")) {
                            destination = line.substring("LOCATION:".length()).trim();
                        } else if (line.startsWith("DTSTART:")) {
                            dtstart = line.substring("DTSTART:".length()).trim();
                        }
                    }

                    // get the date and time formatted
                    String formattedDateTime = null;
                    try {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.US);
                        Date date = inputFormat.parse(dtstart);
                        formattedDateTime = outputFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Display or use the separated variables as needed
                    Toast.makeText(this, "CHECKED IN " + summary, Toast.LENGTH_SHORT).show();

                    // set the event details on the event page
                    title.setText(summary);
                    location.setText(destination);
                    dateAndtime.setText(formattedDateTime);

                    hasScanned = true; // Set the flag to true after successful scan
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}