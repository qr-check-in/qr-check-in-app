package com.example.qrcheckin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateGenerateEventQR extends AppCompatActivity {
    // Main Bar buttons
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;

    // Activity Widgets
    ImageView ivCheckInQR;
    ImageView ivPromoQr;
    Button finishButton;
    Button genPromoQR;
    Button btnGenCheckInQR;
    Button btnUploadCheckInQR;
    private EventDatabaseManager db;
    private String inputEventName;
    private String inputEventDate;
    private String inputEventTime;
    private String inputEventDescription;
    private String inputEventLocation;
    private EventPoster inputEventPoster;
    private String organizerFcm;
    private int numOfAttends;
    QRCode checkInQRCode = null;
    PromoQRCode promoQRCode = null;
    Event incomingEvent;
    private String incomingPosterString;

    // To save image in device
    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;
    boolean checkInQrAvailable = false;
    boolean alreadySaved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event_screen2);

        // Main Bar Widgets
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        addEventButton.setPressed(true);
        profileButton = findViewById(R.id.profileButton);

        // Activity Widgets
        finishButton = findViewById(R.id.finishButton);
        ivCheckInQR = findViewById(R.id.ivCheckInQR);
        ivPromoQr = findViewById(R.id.ivPromoQr);
        btnUploadCheckInQR = findViewById(R.id.btnUploadCheckInQR);
        btnGenCheckInQR = findViewById(R.id.btnGenCheckInQR);

        // ToolBar
        Toolbar toolbar = findViewById(R.id.addEventToolBar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView header = findViewById(R.id.mainHeader);
        header.setText("Create an Event");

        db = new EventDatabaseManager();

        // Get user's fcm token (used for querying the right events in recycler view)
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        organizerFcm = prefs.getString("token", "missing token");

        // Fetch the user's inputs from createNewEventSceen1
        Bundle extras = getIntent().getExtras();
        checkInQRCode = null;
        promoQRCode = null;
        inputEventPoster = null;
        inputEventLocation = null;
        inputEventTime = null;
        inputEventDescription = null;
        numOfAttends = 0;

        // Get the incoming event object
        if (extras != null) {
            incomingEvent = (Event) getIntent().getSerializableExtra("EVENT");

            inputEventPoster = incomingEvent.getPoster();
            inputEventLocation = incomingEvent.getEventLocation();
            inputEventTime = incomingEvent.getEventTime();
            inputEventDescription = incomingEvent.getEventDescription();
            inputEventName = incomingEvent.getEventName();
            inputEventDate = incomingEvent.getEventDate();
            numOfAttends = incomingEvent.getSignupLimit();

            // Retrieve the uri string for the EventPoster
            incomingPosterString = extras.getString("posterString");
        }


        finishButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Creates a new Event upon the finish button being clicked
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (!checkInQrAvailable) {
                    Toast.makeText(CreateGenerateEventQR.this, "Finish generating QR Code", Toast.LENGTH_SHORT).show();
                } else {
                    // Create and store an EventPoster to firestore storage
                    if (incomingPosterString != null) {
                        inputEventPoster = new EventPoster(incomingPosterString, null);
                        ImageStorageManager posterStorage = new ImageStorageManager(inputEventPoster, "/EventPosters");
                        posterStorage.uploadImage();
                    }

                    // Create and store a check-in QR code to firestore storage
                    if (checkInQrAvailable) {
                        ImageStorageManager storageQr = new ImageStorageManager(checkInQRCode, "/QRCodes");
                        storageQr.uploadImage();
                    }

                    Event newEvent = new Event(organizerFcm, checkInQRCode, promoQRCode, inputEventPoster, inputEventName, inputEventDate, inputEventTime, inputEventLocation, inputEventDescription, incomingEvent.isCheckInStatus(), numOfAttends);
                    Log.d("event", String.format("storing event %s", newEvent.getEventName()));
                    db.storeEvent(newEvent);


                    Intent activity = new Intent(getApplicationContext(), EventListView.class);
                    startActivity(activity);
                }
            }
        });

        // Set up listeners for QR code generation/upload/download
        btnGenCheckInQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Too make a combined string and convert into a hashed string (unique) so to generate QR code
                String unhashedContent = inputEventName + inputEventLocation + inputEventDate + inputEventTime;
                generateQRCode(unhashedContent);
            }
        });
        btnUploadCheckInQR.setOnClickListener(new View.OnClickListener() {
            // Listener to add/upload a QR from gallery
            // https://developer.android.com/jetpack/androidx/releases/activity#1.7.0, 2024, how to select a picture from gallery
            @Override
            public void onClick(View v) {
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
                checkInQrAvailable = true;
            }

        });
        ivCheckInQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInQrAvailable) {
                    Intent activity = new Intent(getApplicationContext(), QrCodeImageView.class);
                    activity.putExtra("QRCodeBitmap", bitmap); // Assuming 'bitmap' is the generated QR code bitmap
                    activity.putExtra("EventName&Date", inputEventName + "_" + inputEventDate); // Assuming 'bitmap' is the generated QR code bitmap
                    startActivity(activity);
                }
            }
        });

        // Set up listeners for main app toolbar buttons
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Please Finish Adding Event", Toast.LENGTH_LONG).show();
            }
        });
        qrButton.setOnClickListener(new View.OnClickListener() {
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


    // https://developer.android.com/jetpack/androidx/releases/activity#1.7.0, 2024, how to select a picture from gallery
    // Registers a photo picker activity launcher in single-select mode.
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    // Load the selected image into the ImageView using Glide
                    // openai, 2024, chatgpt, how to display the image
                    Glide.with(this)
                            .load(uri)
                            .into(ivCheckInQR);
                    ivCheckInQR.setVisibility(View.VISIBLE);
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });


    private void generateQRCode(String unhashedContent) {
        try {
            // Get the hashed content of the qr code for the bitmap
            String hashedContent = Utils.hashString(unhashedContent);

            // Generate bitmap for QR code
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(String.valueOf(hashedContent), BarcodeFormat.QR_CODE, 200, 175);

            Bitmap bitmap = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Bitmap.Config.RGB_565);
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.Black) : getResources().getColor(R.color.White));
                }
            }

            ivCheckInQR.setImageBitmap(bitmap);
            ivCheckInQR.setVisibility(View.VISIBLE);

            if (!alreadySaved) {
                String QRCodeUri = saveBitmapImage().toString();
                checkInQRCode = new QRCode(QRCodeUri, null, unhashedContent);
                checkInQrAvailable = true;
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a bitmap to the device and returns its Uri
     * @return Uri of the qr code saved
     */
    private Uri saveBitmapImage() {
        alreadySaved = true;
        bitmapDrawable = (BitmapDrawable) ivCheckInQR.getDrawable();
        bitmap = bitmapDrawable.getBitmap();

        FileOutputStream fileOutputStream = null;

        File sdCard = Environment.getExternalStorageDirectory();
        File Directory = new File(sdCard.getAbsolutePath() + "/Download");
        Directory.mkdir();

        String filename = String.format("%s_%s_%d.jpg", inputEventName, inputEventDate, System.currentTimeMillis());
        File outfile = new File(Directory, filename);

        Toast.makeText(CreateGenerateEventQR.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();

        try {
            fileOutputStream = new FileOutputStream(outfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outfile));
            sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(outfile);
    }


}