package com.example.qrcheckin.Event;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.qrcheckin.ClassObjects.Event;
import com.example.qrcheckin.ClassObjects.EventPoster;
import com.example.qrcheckin.ClassObjects.QRCode;
import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.Common.Utils;
import com.example.qrcheckin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
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
    Button btnGenPromoQR;
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
    QRCode promoQRCode = null;
    Event incomingEvent;
    private String incomingPosterString;

    // To save image in device
    private Bitmap checkInBitmap;
    private Bitmap promoBitmap;

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
        btnGenPromoQR = findViewById(R.id.btnGenPromoQR);

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

        // Fetch the user's inputs from the previous CreateAddEventDetails activity
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

        /*
         * Registers a photo picker activity launcher to upload images.
         * How to select a picture from gallery:
         *      https://developer.android.com/jetpack/androidx/releases/activity#1.7.0
         */
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

        // Set up listeners for QR code generation/upload/download
        btnGenCheckInQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("generateQrCode", "clicked, calling setQRCode");
                if(checkInQRCode == null){
                    String unhashedContent = inputEventName + inputEventLocation + inputEventDate + inputEventTime;
                    checkInQRCode = setQRCode(ivCheckInQR, unhashedContent, false);
                }
                else {
                    Toast.makeText(CreateGenerateEventQR.this, "check-in QR already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener for promotional QR code generator
        btnGenPromoQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(promoQRCode == null){
                    String unhashedContent = inputEventName + inputEventLocation + inputEventDate + inputEventTime + "promo";
                    promoQRCode = setQRCode(ivPromoQr, unhashedContent, true);
                }
                else {
                    Toast.makeText(CreateGenerateEventQR.this, "promotional QR already exists", Toast.LENGTH_SHORT).show();
                }
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
            }

        });

        // Listener for the check-in QR code ImageView
        ivCheckInQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QR code share activity
                shareQRCode(checkInQRCode, checkInBitmap);
            }
        });

        // Listener for the Check-in QR code ImageView
        ivPromoQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QR code share activity
                shareQRCode(promoQRCode, promoBitmap);
            }
        });

        // Set listener to finish creating an event after generating it's QRCode(s)
        finishButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Creates a new Event upon the finish button being clicked
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (checkInQRCode == null) {
                    Toast.makeText(CreateGenerateEventQR.this, "Finish generating QR Code", Toast.LENGTH_SHORT).show();
                } else {
                    // Create and store an EventPoster to firestore storage
                    if (incomingPosterString != null) {
                        inputEventPoster = new EventPoster(incomingPosterString, null);
                        ImageStorageManager posterStorage = new ImageStorageManager(inputEventPoster, "/EventPosters");
                        posterStorage.uploadImage(null);
                    }

                    // Create and store a check-in QR code to firestore storage
                    if (checkInQRCode != null) {
                        ImageStorageManager storageQr = new ImageStorageManager(checkInQRCode, "/QRCodes");
                        storageQr.uploadImage(null);
                    }
                    // Create and store a promotional QRCode to firestore storage
                    if(promoQRCode != null){
                        ImageStorageManager storageQr = new ImageStorageManager(promoQRCode, "/QRCodes");
                        storageQr.uploadImage(null);
                    }

                    // Add the newEvent to the db
                    Event newEvent = new Event(organizerFcm, checkInQRCode, promoQRCode, inputEventPoster, inputEventName, inputEventDate, inputEventTime, inputEventLocation, inputEventDescription, incomingEvent.isCheckInStatus(), numOfAttends);
                    String eventId = db.storeEvent(newEvent);
                    Log.d("EVENT", String.format("Stored event ID:  %s", eventId));

                    // Create topicName for the event
                    String topicName = "event_" + eventId;

                    // Add the topicName to the event
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference eventDocRef = db.collection("events").document(eventId);
                    eventDocRef.update("topicName", topicName);

                    // Organizer subscribes to the topic
                    FirebaseMessaging.getInstance().subscribeToTopic(topicName)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = "Subscribed";
                                    if (!task.isSuccessful()) {
                                        msg = "Subscribe failed";
                                    }
                                    Log.d("Subscription: ", msg);
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });

                    Intent activity = new Intent(getApplicationContext(), EventListView.class);
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

    /**
     *  openai,2024, chatgpt
     *  It returns the event back to the previous page and autofills the data that has been previously inputted
     * @param item The menu item that was selected.
     *
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the up button press
            // Create a new intent to pass the event back to the previous activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("EVENT", incomingEvent);
            setResult(RESULT_OK, resultIntent);
            finish(); // Finish the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Calls methods to get a QR code image and save it to the device, then creates a corresponding QRCode object
     * @param imageView ImageView for the QR Code to be displayed
     * @param unhashedContent String of the event details
     * @param isPromo Boolean indicating if we are creating a promotional QR
     * @return QRCode to check-in/promote the Event being created
     */
    public QRCode setQRCode(ImageView imageView, String unhashedContent, Boolean isPromo){
        // Create and set a check-in or promo QR code only if it doesn't exist yet
        // Generate a QR code bitmap using event details from previous page
        Log.d("generateQrCode", String.format("setQRCode called, making bitmap with %s", unhashedContent));

        Bitmap bitmap = generateQRCode(unhashedContent);
        assert bitmap != null;
        //set the ImageView to the new QR code
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);

        // Create the QR code object and
        String filename = String.format("%s_%s_%d.jpg", inputEventName, inputEventDate, System.currentTimeMillis());
        String QRCodeUri = saveBitmapImage(bitmap, filename).toString();

        // set bitmap attributes
        if (isPromo){
            promoBitmap = bitmap;
        }
        else{
            checkInBitmap = bitmap;
        }
        return new QRCode(QRCodeUri, null, unhashedContent);
    }

    /**
     * Encodes the unhashed content into a QR code image
     * @param unhashedContent   String to encode into QR code
     * @return                  Bitmap of the QR code image OR null (if generation failed)
     */
    private Bitmap generateQRCode(String unhashedContent) {
        try {
            // Get the hashed content of the qr code for the bitmap
            String hashedContent = Utils.hashString(unhashedContent);

            // Generate bitmap for QR code
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(String.valueOf(hashedContent), BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Bitmap.Config.RGB_565);
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.Black) : getResources().getColor(R.color.White));
                }
            }
            return bitmap;  // Return bitmap if it was created successfully

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;        // Return null if generation failed
    }

    /**
     * Saves a bitmap image to the device and returns its Uri
     * @param bitmap    Bitmap of image file to store
     * @param filename  String desired name of image file
     * @return          Uri of the image saved
     */
    private Uri saveBitmapImage(Bitmap bitmap, String filename) {
        // Get directory to store file into
        File sdCard = Environment.getExternalStorageDirectory();
        File Directory = new File(sdCard.getAbsolutePath() + "/Download");
        Directory.mkdir();

        // Set up file (file descriptor) in the directory where the image will be stored
        File outfile = new File(Directory, filename);
        FileOutputStream fileOutputStream = null;

        // Attempt to store image into the file
        try {
            // Write image into the file descriptor
            fileOutputStream = new FileOutputStream(outfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            // Allow the media scanner to access the new file's URI
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outfile));
            sendBroadcast(intent);
            Toast.makeText(CreateGenerateEventQR.this, "QR Code saved to downloads folder", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("SaveBitmapError", "Error saving bitmap to device\n", e);
        }

        return Uri.fromFile(outfile);
    }

    /**
     * Starts activity to share a QR Code
     * @param qrCode QRCode to be shared
     * @param bitmap bitmap of the QRCode to be shared
     */
    public void shareQRCode(QRCode qrCode, Bitmap bitmap){
        if (qrCode != null) {
            Intent activity = new Intent(getApplicationContext(), QrCodeImageView.class);
            activity.putExtra("QRCodeBitmap", bitmap);
            activity.putExtra("EventName&Date", inputEventName + "_" + inputEventDate);
            startActivity(activity);
        }
    }

}