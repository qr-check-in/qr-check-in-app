package com.example.qrcheckin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.processing.SurfaceProcessorNode;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class QrCodeImageView extends AppCompatActivity {

    // MainBar declarations
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    ImageButton backButton;

    ImageView qrCodeImage;
    ImageButton shareImage;
    String eventText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_image_view);

        TextView header = findViewById(R.id.mainHeader);
        header.setText("Share QR Code");

        // Initialize Mainbar Attributes
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        backButton = findViewById(R.id.goBack);

        qrCodeImage = findViewById(R.id.qrCodeImageView);
        shareImage = findViewById(R.id.shareQR);

        // Retrieve the QR code bitmap from the Intent extras
        Bitmap qrCodeBitmap = getIntent().getParcelableExtra("QRCodeBitmap");
        // Retrieve event name and date
        eventText = getIntent().getStringExtra("EventName&Date");
        Log.d("eventText", "Event Name" + eventText);

        // Display the QR code bitmap in an ImageView
        qrCodeImage.setImageBitmap(qrCodeBitmap);



        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) qrCodeImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageandText(bitmap);

            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void shareImageandText(Bitmap bitmap) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");

        Uri uri = null;
        uri = getImageToShare(bitmap, getApplicationContext());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Scan and Share this QR Code to attend an event.");
        intent.putExtra(Intent.EXTRA_TEXT, eventText);
        startActivity(Intent.createChooser(intent, "Share via"));

    }


    private Uri getImageToShare(Bitmap image, Context context) {
        File folder = new File (context.getCacheDir(), "images");
        Uri uri = null;

        try{
            folder.mkdirs();
            File file = new File(folder, eventText + ".jpg");

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG,90, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
            uri = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()), "com.example.qrcheckin" + ".provider", file);
        }
        catch (IOException e){
            Log.d("getImageToShare Error", "Exception" + e.getMessage());
        }
        return uri;
    }
}