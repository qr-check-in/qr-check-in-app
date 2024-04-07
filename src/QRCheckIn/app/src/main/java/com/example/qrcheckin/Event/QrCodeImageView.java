package com.example.qrcheckin.Event;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.R;

import java.io.File;
import java.io.FileOutputStream;

public class QrCodeImageView extends AppCompatActivity {

    // MainBar declarations
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;

    ImageView qrCodeImage;
    ImageButton shareImage;
    String eventText;
    private QRCode qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_image_view);

        // Initialize Mainbar Attributes
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);

        qrCodeImage = findViewById(R.id.qrCodeImageView);
        shareImage = findViewById(R.id.shareQR);

        // Retrieve the QR code bitmap from the Intent extras
        //Bitmap qrCodeBitmap = getIntent().getParcelableExtra("QRCodeBitmap");
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            qrCode = (QRCode) getIntent().getSerializableExtra("QRCode");
            Log.d("SHARE QR", String.format("after passing image uri is %s", qrCode.getUriString()));
        }

        ImageStorageManager storage = new ImageStorageManager(qrCode, "/QRCodes");
        storage.displayImage(qrCodeImage);
        // Retrieve event name and date
        eventText = getIntent().getStringExtra("eventName&Date");

        // Display the QR code bitmap in an ImageView
        //qrCodeImage.setImageBitmap(qrCodeBitmap);

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
                BitmapDrawable bitmapDrawable = (BitmapDrawable) qrCodeImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageandText(bitmap);
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

    private void shareImageandText(Bitmap bitmap) {
        //Uri uri = getImageToShare(bitmap);
        Uri uri = Uri.parse(qrCode.getUriString());
        Log.d("SHARE", String.format("share uri %s", qrCode.getUriString()));
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_TEXT, eventText);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Scan and Share this QR Code to attend an event.");
        //intent.setType("qrCode/*");
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Share via"));
        } else {
            // Handle case where no apps are available
            Toast.makeText(this, "No apps available to handle this action", Toast.LENGTH_SHORT).show();
        }

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