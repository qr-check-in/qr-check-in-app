package com.example.qrcheckin;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Display detailed info about specific event.
 * Retrieves & displays event details from Firestore database.
 * Provide users with information about event; name, date, location, description, images.
 */
public class OrganizersEventPageActivity extends AppCompatActivity {
    // Mainbar
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;

    // View widgets
    CheckBox signupCheckBox;
    TextView signupLimitReached;
    ImageButton openBottomSheetBtn;
    private EventDatabaseManager eventDb;
    private String fcmToken;
    private String documentId;

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
        setContentView(R.layout.activity_organizers_event_screen);

        // Set and display the main bar
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);

        // Manage Toolbar
        Toolbar toolbar = findViewById(R.id.organizer_eventScreen_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView header = findViewById(R.id.mainHeader);

        eventButton.setPressed(true);

        // Find the text views on the event page xml
        TextView tvEventDate = findViewById(R.id.text_event_date);
        TextView tvEventLocation = findViewById(R.id.text_event_location);
        TextView tvEventDescription = findViewById(R.id.text_event_description);
        ImageView ivEventPoster = findViewById(R.id.image_event_poster);
        ImageView ivEventPromoQr = findViewById(R.id.image_event_promo_qr);
        openBottomSheetBtn = findViewById(R.id.openBottomSheetButton);
        signupCheckBox = findViewById(R.id.signup_button);
        signupLimitReached = findViewById(R.id.signup_limit_text);
        signupLimitReached.setVisibility(View.INVISIBLE);

        // Set open Bottom Sheet Listner
        openBottomSheetBtn.setOnClickListener(v -> {
            showDialog();
        });

        // Retrieve the user's fcmToken/ attendee docID
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        fcmToken = prefs.getString("token", "missing token");

        // Retrieve the event passed from the previous activity
        documentId = getIntent().getStringExtra("DOCUMENT_ID");

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

        // Set listener for the signup CheckBox
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

        // Set the "Scan QR code" toolbar button listener
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

    }

    /**
     * Displays either the CheckBox, allowing users to signup and un-signup for the event, or displays a TextView
     * if the event has reached its limit
     * @param signupLimit int of the maximum number of attendees who can sign up for the event
     * @param signups ArrayList of Strings containing the attendee doc IDs of those who are signed-up for the event
     */
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

    /**
     * Opens the Bottom Sheet to access Organizer Options for their event
     * https://www.youtube.com/watch?v=sp9j0e-Kzc8&t=472s, 2024, how to implement a bottom sheet
     */
    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        LinearLayout editEventDetails = dialog.findViewById(R.id.editEventDetails);
        LinearLayout createEventNotification = dialog.findViewById(R.id.createEventNotification);
        LinearLayout viewEventSignups = dialog.findViewById(R.id.viewSignedUp);
        LinearLayout viewEventParticipants = dialog.findViewById(R.id.viewEventCheckin);

        editEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Feature not implemented",Toast.LENGTH_SHORT).show();

            }
        });

        createEventNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), CreateNotification.class);
                startActivity(event);

            }
        });

        // Listener for the View Event Signups layout
        viewEventSignups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), AttendeeList.class);
                intent.putExtra("EVENT_DOC_ID", documentId);
                intent.putExtra("FIELD_NAME", "signupEvents");
                startActivity(intent);
            }
        });

        // Listener for the View Event Participants layout (checked-in attendees)
        viewEventParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), AttendeeList.class);
                intent.putExtra("EVENT_DOC_ID", documentId);
                intent.putExtra("FIELD_NAME", "attendedEvents");
                startActivity(intent);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}