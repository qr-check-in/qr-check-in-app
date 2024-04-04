package com.example.qrcheckin.Event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcheckin.R;

import java.io.File;
import java.io.FileOutputStream;

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
        header.setText("QR Code Image");

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
        eventText = getIntent().getStringExtra("eventName&Date");

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
        Uri uri = getImageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, eventText);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Scan and Share this QR Code to attend an event.");
        intent.setType("qrCode/*");
        startActivity(Intent.createChooser(intent, "Share via"));

    }

    private Uri getImageToShare(Bitmap bitmap) {
        File folder = new File (getCacheDir(), "images");
        Uri uri = null;

        try{
            Log.d("folder", String.format("created: " + folder.mkdirs() ));

            folder.mkdirs();
            File file = new File(folder, "qrCode.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG,90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(this, "com.example.qrcheckin", file);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return uri;
    }
}