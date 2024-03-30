package com.example.qrcheckin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Display detailed info about specific event.
 * Retrieves & displays event details from Firestore database.
 * Provide users with information about event; name, date, location, description, images.
 */
public class AdminEventPage extends AppCompatActivity {
    // Mainbar

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EventDatabaseManager eventDb;
    CheckBox signupCheckBox;
    private TextView tvEventName, tvEventDate, tvEventLocation, tvEventDescription;
    TextView signupLimitReached;
    private ImageView ivEventPoster, ivEventPromoQr;

    private String fcmToken;
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
        initializeViews();
        // Find the text views on the event page xml

        Button back = findViewById(R.id.back_button);
        signupCheckBox = findViewById(R.id.signup_button);
        signupLimitReached = findViewById(R.id.signup_limit_text);
        signupLimitReached.setVisibility(View.INVISIBLE);
        // Retrieve the event passed from the previous activity
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("DOCUMENT_ID");
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        fcmToken = prefs.getString("token", "missing token");

        if (documentId != null && !documentId.isEmpty()) {

            fetchEventDetails(documentId);
        } else {
            Log.e("AdminEventPage", "Document ID is null or empty.");
            finish(); // Exit the activity if no document ID is provided
        }
        signupCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AttendeeDatabaseManager attendeeDb = new AttendeeDatabaseManager(fcmToken);
                // Update signup array in event document
                if (isChecked){
                    eventDb.addToArrayField("signups", fcmToken);
                    attendeeDb.addToArrayField("signupEvents", documentId);
                }
                else{
                    eventDb.removeFromArrayField("signups", fcmToken);
                    attendeeDb.removeFromArrayField("signupEvents", documentId);
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
    private void fetchEventDetails(String documentId) {
        db.collection("events").document(documentId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Event event = documentSnapshot.toObject(Event.class);
                if (event != null) {
                    displayEventDetails(event);

                } else {
                    Log.e("AdminEventPage", "Failed to parse the event details.");
                }
            } else {
                Log.e("AdminEventPage", "No such document with id: " + documentId);
            }
        }).addOnFailureListener(e -> Log.e("AdminEventPage", "Error fetching document", e));
    }
    public void setSignupCheckBox(int signupLimit, ArrayList<String> signups){
        // Set status of the checkbox
        boolean userInSignups = signups != null && signups.contains(fcmToken);
        signupCheckBox.setChecked(userInSignups);

        // Set visibilities of CheckBox and limit reached TextView
        if(userInSignups || Objects.requireNonNull(signups).size() != signupLimit){
            // User is signed up for the event, so they see the checkbox (in case they want to un-signup)
            // Or the signup limit is not reached, so checkbox is visible to anyone
            signupCheckBox.setVisibility(View.VISIBLE);
            signupLimitReached.setVisibility(View.INVISIBLE);
        }
        else{
            // User is not in signups list AND the limit is reached, show the limit reached text
            signupCheckBox.setVisibility(View.INVISIBLE);
            signupLimitReached.setVisibility(View.VISIBLE);
        }

    }
    private void initializeViews() {
        tvEventName = findViewById(R.id.text_event_name);
        tvEventDate = findViewById(R.id.text_event_date);
        tvEventLocation = findViewById(R.id.text_event_location);
        tvEventDescription = findViewById(R.id.text_event_description);
        ivEventPoster = findViewById(R.id.image_event_poster);
        ivEventPromoQr = findViewById(R.id.image_event_promo_qr);
        signupCheckBox = findViewById(R.id.signup_button);
        signupLimitReached = findViewById(R.id.signup_limit_text);
        signupLimitReached.setVisibility(View.INVISIBLE);
    }
    private void displayEventDetails(Event event) {
        //tvEventName.setText(event.getEventName());
        tvEventLocation.setText(event.getEventLocation());
        tvEventDate.setText(event.getEventDate());
        tvEventDescription.setText(event.getEventDescription());

        // Assuming you have methods to handle displaying images in ivEventPoster and ivEventPromoQr
        // For example:
        // loadImageIntoView(event.getPosterURL(), ivEventPoster);
    }
}