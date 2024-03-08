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


public class QRCodeScan extends AppCompatActivity implements CheckoutFragment.CheckOutDialogListener{

    private TextView description;
    private TextView title;
    private TextView location;
    private TextView dateAndtime;
    private ImageButton goBack;
    private boolean hasScanned = false; // Boolean flag to track whether a scan has been performed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);

        title = findViewById(R.id.topNavigationText);
        location = findViewById(R.id.location);
        dateAndtime = findViewById(R.id.dateAndtime);
        description = findViewById(R.id.eventDescription);
        goBack = findViewById(R.id.back);


        //Opens the scanner to scan the QRCode
        startScanner();


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a new Bundle to hold the data
                Bundle bundle = new Bundle();

                // Put different types of data into the bundle
                bundle.putString("title", title.getText().toString());

                // Create a new instance of AddCityFragment
                CheckoutFragment fragment = new CheckoutFragment();

                // Set the bundle as arguments for the fragment
                fragment.setArguments(bundle);

                // Show the fragment
                fragment.show(getSupportFragmentManager(), "Confirming Checkout");

            }
        });
    }

    // Open the Scanner to scan QR code
    private void startScanner() {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QR code");
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!hasScanned) { // Only proceed if scanning hasn't been performed yet
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Separate the scanned data into different variables
                    String scannedData = result.getContents();
                    String[] lines = scannedData.split("\n");
                    String summary = null, detail = null, destination = null, dtstart = null;

                    for (String line : lines) {
                        if (line.startsWith("TITLE:")) {
                            summary = line.substring("TITLE:".length()).trim();
                        } else if (line.startsWith("DESCRIPTION:")) {
                            detail = line.substring("DESCRIPTION:".length()).trim();
                        } else if (line.startsWith("LOCATION:")) {
                            destination = line.substring("LOCATION:".length()).trim();
                        } else if (line.startsWith("DTSTART:")) {
                            dtstart = line.substring("DTSTART:".length()).trim();
                        }
                    }

                    // Format date and time
                    String formattedDateTime = null;
                    try {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'Z'");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.US);
                        Date date = inputFormat.parse(dtstart);
                        formattedDateTime = outputFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Display or use the separated variables as needed
                    Toast.makeText(this, "CHECKED IN " + summary, Toast.LENGTH_SHORT).show();

                    description.setText(detail);
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

    //CheckOutDialogListener for yes OR no confirmation of CheckOut?
    @Override
    public void onCheckOutConfirmed() {
        Toast.makeText(QRCodeScan.this, "CHECKED OUT OF " + title.getText().toString(), Toast.LENGTH_SHORT).show();
        finish();
    }
}