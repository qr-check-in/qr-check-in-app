 package com.example.qrcheckin;

 import android.content.Intent;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.View;
 import android.widget.Button;
 import android.widget.ImageButton;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.Toolbar;

 import java.util.UUID;

 public class createNewEventScreen2 extends AppCompatActivity {
     ImageButton qrButton;
     ImageButton eventButton;
     ImageButton addEventButton;
     ImageButton profileButton;
     Button finishButton;

     private Database db;
     private String inputEventName;
     private String inputEventDate;
     Event incomingEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event_screen2);

        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        addEventButton.setPressed(true);
        profileButton = findViewById(R.id.profileButton);
        finishButton = findViewById(R.id.finishButton);

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
}