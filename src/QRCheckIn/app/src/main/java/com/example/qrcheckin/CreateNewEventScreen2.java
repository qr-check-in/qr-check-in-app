 package com.example.qrcheckin;

 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.content.pm.PackageManager;
 import android.database.Cursor;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.graphics.drawable.BitmapDrawable;
 import android.net.Uri;
 import android.os.Bundle;
 import android.os.Environment;
 import android.provider.MediaStore;
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
 import androidx.annotation.NonNull;
 import androidx.annotation.Nullable;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.Toolbar;
 import androidx.core.app.ActivityCompat;
 import androidx.core.content.ContextCompat;

 import com.bumptech.glide.Glide;
 import com.google.zxing.BarcodeFormat;
 import com.google.zxing.WriterException;
 import com.google.zxing.common.BitMatrix;
 import com.journeyapps.barcodescanner.BarcodeEncoder;

 import java.io.File;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.util.UUID;
 import android.Manifest;
 import java.security.Permission;

 import org.jetbrains.annotations.NotNull;

 public class CreateNewEventScreen2 extends AppCompatActivity {
     // Main Bar buttons
     ImageButton qrButton;
     ImageButton eventButton;
     ImageButton addEventButton;
     ImageButton profileButton;

     // Activity Widgets
     ImageView checkInQR;
     ImageView promotionalQR;
     Button finishButton;
     Button genPromoQR;
     Button genCheckInQR;
     Button uploadQR;
     private Database db;
     private String inputEventName;
     private String inputEventDate;
     private String inputEventTime;
     private String inputEventDescription;
     private String inputEventLocation;
     private EventPoster inputEventPoster;
     QrCode checkInQRCode = null;
     PromoQRCode promoQRCode = null;
     Event incomingEvent;
     private String incomingPosterString;

     // To save image in device
     Bitmap bitmap;
     BitmapDrawable bitmapDrawable;
     boolean qrCodeAvailable = false;



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
        checkInQR = findViewById(R.id.checkInQR);
        promotionalQR = findViewById(R.id.promotionalQR);
        uploadQR = findViewById(R.id.uploadQRCR);
        genCheckInQR = findViewById(R.id.makeQRCI);

        // ToolBar
        Toolbar toolbar = findViewById(R.id.addEventToolBar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView header = findViewById(R.id.mainHeader);
        header.setText("Create an Event");

        db = new Database();

        // Fetch the user's inputs from createNewEventSceen1
        Bundle extras = getIntent().getExtras();
        checkInQRCode = null;
        promoQRCode = null;
        inputEventPoster = null;
        inputEventLocation = null;
        inputEventTime = null;
        inputEventDescription = null;

        if (extras != null) {
//          Get the incoming event object
            incomingEvent = (Event) getIntent().getSerializableExtra("EVENT");
            // Move the info of the new
//        checkInQRCode = incomingEvent.getCheckInQRCode();
//        promoQRCode = incomingEvent.getPromoQRCode();
            // Info from the previous page
            inputEventPoster = incomingEvent.getPoster();
            inputEventLocation = incomingEvent.getEventLocation();
            inputEventTime = incomingEvent.getEventTime();
            inputEventDescription = incomingEvent.getEventDescription();
            inputEventName = incomingEvent.getEventName();
            inputEventDate = incomingEvent.getEventDate();
            //Log.d("event", String.format("passed event %s %s", inputEventName, inputEventDate));

            // Retrieve the uri string for the EventPoster
            incomingPosterString = extras.getString("posterString");
        }


        checkInQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrCodeAvailable){
                    Intent activity = new Intent(getApplicationContext(), QrCodeImageView.class);
                    activity.putExtra("QRCodeBitmap", bitmap); // Assuming 'bitmap' is the generated QR code bitmap
                    activity.putExtra("EventName&Date", inputEventName + "_" + inputEventDate); // Assuming 'bitmap' is the generated QR code bitmap
                    startActivity(activity);
                }
            }
        });


        // Listener to add/upload a QR from gallery
        // https://developer.android.com/jetpack/androidx/releases/activity#1.7.0, 2024, how to select a picture from gallery
        uploadQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }

        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Creates a new Event upon the finish button being clicked
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                genCheckInQR.performClick();

                UUID eventId = UUID.randomUUID();

                // Create and store an EventPoster to firestore storage
                EventPoster inputEventPoster = new EventPoster(incomingPosterString, null);
                inputEventPoster.uploadImage("/EventPosters", incomingPosterString);

                Event newEvent = new Event(checkInQRCode, promoQRCode, inputEventPoster, inputEventName, inputEventDate, inputEventTime, inputEventLocation, inputEventDescription, incomingEvent.isCheckInStatus());
                Log.d("event", String.format("storing event %s", newEvent.getEventName()));
                db.storeEvent(newEvent);

                Intent activity = new Intent(getApplicationContext(), EventListView.class);
                startActivity(activity);
            }
        });

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

        genCheckInQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Too make a combined string and convert into a hashed string (unique) so to generate QR code
                String combinedContent = inputEventName + inputEventLocation + inputEventDate + inputEventTime;
                Log.d("CombinedContent", "CombinedContent: " + combinedContent);

                // Create a new QrCode object with combinedContent as unhashed content
                checkInQRCode = new QrCode(null, null, combinedContent);

                String hashedContent = checkInQRCode.getHashedContent();
                Log.d("HashedContent", "HashedContent: " + hashedContent);

                generateQRCode(hashedContent);
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
                             .into(checkInQR);
                     checkInQR.setVisibility(View.VISIBLE);
                     Log.d("PhotoPicker", "Selected URI: " + uri);
                 } else {
                     Log.d("PhotoPicker", "No media selected");
                 }
             });


     private void generateQRCode(String hashedContent) {
         try {
             BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
             BitMatrix bitMatrix = barcodeEncoder.encode(String.valueOf(hashedContent), BarcodeFormat.QR_CODE, 200, 175);

             Bitmap bitmap = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Bitmap.Config.RGB_565);
             for (int x = 0; x < bitMatrix.getWidth(); x++) {
                 for (int y = 0; y < bitMatrix.getHeight(); y++) {
                     bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.Black) : getResources().getColor(R.color.White));
                 }
             }

             checkInQR.setImageBitmap(bitmap);
             checkInQR.setVisibility(View.VISIBLE);

             qrCodeAvailable = true;
             saveImage();

         } catch (WriterException e) {
             e.printStackTrace();
         }
     }

    // To save image of QR Code generated in the device
     private void saveImage(){
         bitmapDrawable = (BitmapDrawable) checkInQR.getDrawable();
         bitmap = bitmapDrawable.getBitmap();

         FileOutputStream fileOutputStream = null;

         File sdCard = Environment.getExternalStorageDirectory();
         File Directory = new File(sdCard.getAbsolutePath() + "/Download");
         Directory.mkdir();

         String filename = String.format("%s_%s_%d.jpg", inputEventName, inputEventDate, System.currentTimeMillis());
         File outfile = new File(Directory,filename);

         Toast.makeText(CreateNewEventScreen2.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();

         try {
             fileOutputStream = new FileOutputStream(outfile);
             bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
             fileOutputStream.flush();
             fileOutputStream.close();

             Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
             intent.setData(Uri.fromFile(outfile));
             sendBroadcast(intent);

         }
         catch (FileNotFoundException e){
             e.printStackTrace();
         }
         catch (IOException e){
             e.printStackTrace();
         }
     }
}