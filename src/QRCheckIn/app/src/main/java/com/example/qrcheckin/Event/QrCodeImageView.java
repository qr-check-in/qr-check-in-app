
package com.example.qrcheckin.Event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.R;

public class QrCodeImageView extends AppCompatActivity {

    // MainBar declarations
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;

    ImageView qrCodeImage;
    ImageButton shareImage;
    private QRCode qrCode;
    String eventName;
    String eventDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_image_view);

        // Initialize Mainbar Attributes
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);

        qrCodeImage = findViewById(R.id.eventPosterImageView);
        shareImage = findViewById(R.id.shareQR);

        // Retrieve the QR code from the Intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            qrCode = (QRCode) getIntent().getSerializableExtra("QRCode");
        }
        // Display the QR code bitmap in an ImageView
        ImageStorageManager storage = new ImageStorageManager(qrCode, "/QRCodes");
        storage.displayImage(qrCodeImage);
        // Retrieve event name
        eventName = getIntent().getStringExtra("EventName");
        // Retrieve event date
        eventDate = getIntent().getStringExtra("EventDate");

        // ToolBar
        Toolbar toolbar = findViewById(R.id.addEventToolBar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView header = findViewById(R.id.mainHeader);

        // Retrieves the header text to show if the QR code is a check-in or promotional code
        String headerText = getIntent().getStringExtra("headerText");
        header.setText(headerText);

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImageAndText();
            }
        });


        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Please Finish Adding Event", Toast.LENGTH_LONG).show();
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Please Finish Adding Event", Toast.LENGTH_LONG).show();
            }
        });


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Please Finish Adding Event", Toast.LENGTH_LONG).show();
            }
        });
    }

    // openai, 2024, chatgpt: how to define a different action for the back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                // Define the behavior when the back button is pressed
                // For example, navigate back to the previous activity or perform any other action
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Takes the image in the QR Code ImageView and start a share sheet activity to share
     * a QR code on other apps.
     */
    private void shareImageAndText() {
        // Get the image from the QR code ImageView
        // https://stackoverflow.com/questions/44178771/how-can-i-share-an-image-from-firebase-to-whatsapp-instagram-etc-android-studi#comment75382216_44178771 , Amr, 2018
        ImageView content = findViewById(R.id.eventPosterImageView);
        content.setDrawingCacheEnabled(true);
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), content.getDrawingCache(), "title", "description"));
        Log.d("SHARE", String.format("share uri %s", qrCode.getUriString()));
        // Start new share activity to share an image
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_TEXT, "Scan and Share QR Code for " + eventName);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Attend " + eventName + " on " + eventDate + ".");
        startActivity(Intent.createChooser(intent, "Share via"));
    }
}