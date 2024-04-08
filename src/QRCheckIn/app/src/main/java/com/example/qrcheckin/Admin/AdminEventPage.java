package com.example.qrcheckin.Admin;

import static android.content.ContentValues.TAG;

import com.example.qrcheckin.Common.Image;
import com.example.qrcheckin.Event.Event;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qrcheckin.Attendee.AttendeeDatabaseManager;
import com.example.qrcheckin.Attendee.ProfileActivity;
import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.Common.MainActivity;
import com.example.qrcheckin.Event.CreateAddEventDetails;
import com.example.qrcheckin.Event.EventDatabaseManager;
import com.example.qrcheckin.Event.EventListView;
import com.example.qrcheckin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Display detailed info about specific event.
 * Retrieves & displays event details from Firestore database.
 * Provide users with information about event; name, date, location, description, images.
 */
public class AdminEventPage extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EventDatabaseManager eventDb;
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    CheckBox signupCheckBox;
    TextView signupLimitReached;
    Admin admin;
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
        qrButton = findViewById(R.id.qrButton); // Make sure you have a correct ID here
        eventButton = findViewById(R.id.calenderButton); // Make sure you have a correct ID here
        addEventButton = findViewById(R.id.addCalenderButton); // This ID needs to be in your layout
        profileButton = findViewById(R.id.profileButton);
        //initializeViews();
        // Find the text views on the event page xml
        TextView tvEventDate = findViewById(R.id.text_event_date);
        TextView tvEventLocation = findViewById(R.id.text_event_location);
        TextView tvEventDescription = findViewById(R.id.text_event_description);
        ImageView ivEventPoster = findViewById(R.id.image_event_poster);
        ImageView ivEventPromoQr = findViewById(R.id.image_event_promo_qr);
        Button removeEvent = findViewById(R.id.btnRemoveEvent);
        Button back = findViewById(R.id.back_button);
        signupCheckBox = findViewById(R.id.signup_button);
        signupLimitReached = findViewById(R.id.signup_limit_text);
        signupLimitReached.setVisibility(View.INVISIBLE);
        TextView header = findViewById(R.id.mainHeader);
        qrButton.setOnClickListener(v -> {
            Intent event = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(event);
        });

        // Set the "Add event" toolbar button listener
        addEventButton.setOnClickListener(v -> {
            Intent event = new Intent(getApplicationContext(), CreateAddEventDetails.class);
            startActivity(event);
        });

        // Set the "Profile" toolbar button listener
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(event);

            }
        });
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), EventListView.class);
                startActivity(event);
            }
        });
        admin = new Admin();
        // Retrieve the event passed from the previous activity
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("DOCUMENT_ID");
        if(documentId == null) {
            Log.e(TAG, "Document ID is null");
            // Handle the error, maybe finish the activity
            finish();
            return;
        }
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        fcmToken = prefs.getString("token", "missing token");
        Log.d(TAG, "Document ID: " + documentId + ", FCM Token: " + fcmToken);
        eventDb = new EventDatabaseManager(documentId);
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
        removeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.deleteEvent(documentId);
                Intent event = new Intent(getApplicationContext(), AdminViewEvent.class);
                startActivity(event);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), AdminViewEvent.class);
                startActivity(event);
            }
        });
        //documentId = getIntent().getStringExtra("DOCUMENT_ID");

        eventDb = new EventDatabaseManager(documentId);
        eventDb.getDocRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Get and display event details
                Event event = documentSnapshot.toObject(Event.class);
                if (documentSnapshot != null && event != null) {
                    header.setText(event.getEventName());
                    tvEventLocation.setText(event.getEventLocation());
                    tvEventDate.setText(event.getEventDate());
                    tvEventDescription.setText(event.getEventDescription());
                    setSignupCheckBox(event.getSignupLimit(), event.getSignups());
                    // Set the ImageView for the Event's poster
                    if (event.getPoster() != null){
                        ImageStorageManager storagePoster = new ImageStorageManager(event.getPoster(), "/EventPosters");
                        storagePoster.displayImage(ivEventPoster);
                    }
                    // Set the ImageView for the Event's QR code
                    // TODO: Display promo QR instead of check-in QR
                    if (event.getCheckInQRCode() != null) {
                        ImageStorageManager storageQr = new ImageStorageManager(event.getCheckInQRCode(), "/QRCodes");
                        storageQr.displayImage(ivEventPromoQr);
                    }
                } else {
                    Log.d("Firestore", String.format("No such document with id %s", documentId));
                }

            }
        });

        // Listen for updates to the event document, for cases where another user signs up for the event while this page is loaded
        eventDb.getDocRef().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }
                if (value != null && value.exists()) {
                    Log.d(TAG, "Current data: " + value.getData());
                    Event event = value.toObject(Event.class);
                    assert event != null;
                    // Update the CheckBox upon a change in the event doc
                    setSignupCheckBox(event.getSignupLimit(), event.getSignups());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
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
    public void deleteEventPoster(String eventId) {
        // Assuming dbManager is already configured to interact with Firestore
        eventDb.getDocRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Event event = documentSnapshot.toObject(Event.class);
                if (event == null) {
                    Log.e("Firestore", "Event doc not found for ID: " + eventId);
                } else {
                    Image posterPath = event.getPoster();
                    if (posterPath != null && posterPath.getUriString()!=null) {
                        ImageStorageManager storage = new ImageStorageManager(posterPath, "/EventPosters");
                        // Remove event poster from storage
                        storage.deleteImage();
                        // update event doc to remove poster path
                        event.setPoster(null);

                        // Push the updated event object back to Firestore
                        eventDb.getDocRef().set(event)
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Event poster reference removed successfully."))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating event poster reference", e));
                    } else {
                        Log.d("Firestore", "No poster to delete for event ID: " + eventId);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Log.e("Firestore", "Failed to fetch event for poster deletion: " + e.getMessage());
            }
        });
    }


}