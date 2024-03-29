package com.example.qrcheckin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
/**
 * Display detailed info about specific event.
 * Retrieves & displays event details from Firestore database.
 * Provide users with information about event; name, date, location, description, images.
 */
public class AdminEventPage extends AppCompatActivity {
    // Mainbar

    private EventDatabaseManager eventDb;
    /**
     * Init activity, sets content view, and configures the toolbar with navigation buttons.
     * Retrieves & displays event details from Firestore based on the passed document ID.
     *
     * @param savedInstanceState If activity re-initialized after previously being shut down,
     *                           contains data most recently supplied.
     *                           Otherwise its null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_activity);

        // Set and display the main bar


        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Find the text views on the event page xml
        TextView tvEventName = findViewById(R.id.text_event_name);
        TextView tvEventDate = findViewById(R.id.text_event_date);
        TextView tvEventLocation = findViewById(R.id.text_event_location);
        TextView tvEventDescription = findViewById(R.id.text_event_description);
        ImageView ivEventPoster = findViewById(R.id.image_event_poster);
        ImageView ivEventPromoQr = findViewById(R.id.image_event_promo_qr);
        Button back = findViewById(R.id.btnBack);

        // Retrieve the event passed from the previous activity
        String documentId = getIntent().getStringExtra("DOCUMENT_ID");
        eventDb = new EventDatabaseManager(documentId);
        eventDb.getEventDocRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Get and display event details
                Event event = documentSnapshot.toObject(Event.class);
                if (documentSnapshot != null && event != null) {
                    tvEventName.setText(event.getEventName());
                    tvEventLocation.setText(event.getEventLocation());
                    tvEventDate.setText(event.getEventDate());
                    tvEventDescription.setText(event.getEventDescription());
                    // Set the ImageView for the Event's poster
                    if (event.getPoster() != null){
                        ImageStorageManager storage = new ImageStorageManager();
                        storage.displayImage(event.getPoster(), "/EventPosters/",ivEventPoster);
                    }
                } else {
                    Log.d("Firestore", String.format("No such document with id %s", documentId));
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), AdminViewEvent.class);
                startActivity(event);
            }
        });

    }
}