 package com.example.qrcheckin;

 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;
 import android.widget.ImageButton;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.Toolbar;

 import com.google.firebase.firestore.CollectionReference;
 import com.google.firebase.firestore.FirebaseFirestore;

 import java.util.UUID;

 public class createNewEventScreen2 extends AppCompatActivity {
     ImageButton qrButton;
     ImageButton eventButton;
     ImageButton addEventButton;
     ImageButton profileButton;
     Button finishButton;

     private FirebaseFirestore db;
     private CollectionReference eventsRef;

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

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        /**
         * Creates a new Event upon the finish button being clicked
         */
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID eventId = UUID.randomUUID();

                // TEMPORARY: initializing event attributes to null to create a new Event
                // should actually be initialized to the users' inputs
                QRCode checkInQRCode = null;
                PromoQRCode promoQRCode = null;
                EventPoster eventPoster = null;
                String eventName = "testname3";
                String eventTime = "testtime3";
                String eventLocation = null;
                String eventDescription = null;

                Event event = new Event(eventId, checkInQRCode, promoQRCode, eventPoster, eventName, eventTime, eventLocation, eventDescription);
                eventsRef.document().set(event);
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

    /* may be needed for storing custom object attributes, for now
    eventsRef.document().set(event);
    in the finishButton click listener is enough to add the event object to the db

    public void addNewEvent(Event event){
        //HashMap<String, String> eventData = new HashMap<>();
        //eventData.put("eventName", event.getEventName());
        //eventsRef.document(event.getEventId().toString()).set(eventData);
        eventsRef.document().set(event);
    }
     */
}