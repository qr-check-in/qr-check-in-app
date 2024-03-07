 package com.example.qrcheckin;

 import android.content.Intent;
 import android.os.Bundle;
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

 import java.util.UUID;

 public class createNewEventScreen2 extends AppCompatActivity {
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
     Event incomingEvent;

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
        if (extras != null) {
//            inputEventName = extras.getString("eventName");
//            inputEventDate = extras.getString("eventDate");
//            Get the incoming event object
            incomingEvent = (Event) getIntent().getSerializableExtra("newEvent");
            //Log.d("event", String.format("passed event %s %s", inputEventName, inputEventDate));
        }

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
                UUID eventId = UUID.randomUUID();

                // TEMPORARY: initializing event attributes to null to create a new Event
                // TODO: set all event attributes to user's inputs
                QRCode checkInQRCode = null;
                PromoQRCode promoQRCode = null;
                EventPoster eventPoster = null;
                String eventLocation = null;
                String eventTime = null;
                String eventDescription = null;

                // Move the info of the new
                checkInQRCode = incomingEvent.getCheckInQRCode();
                promoQRCode = incomingEvent.getPromoQRCode();
                // Info from the previous page
                eventPoster = incomingEvent.getPoster();
                eventLocation = incomingEvent.getEventLocation();
                eventTime = incomingEvent.getEventTime();
                eventDescription = incomingEvent.getEventDescription();
                inputEventName = incomingEvent.getEventName();
                inputEventDate = incomingEvent.getEventDate();

                Event newEvent = new Event(eventId, checkInQRCode, promoQRCode, eventPoster, incomingEvent.getEventName(), incomingEvent.getEventDate(), eventTime, eventLocation, eventDescription, incomingEvent.isCheckInStatus());
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
}