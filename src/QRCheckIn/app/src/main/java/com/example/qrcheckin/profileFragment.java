package com.example.qrcheckin;

import static com.example.qrcheckin.R.layout.show_profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profileFragment extends AppCompatActivity {
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    Button updatePicture;
    Button removePicture;
    TextView tvName;
    TextView tvContact;
    TextView tvHomepage;
    Switch switchGeolocation;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference attendeesRef = db.collection("Attendees");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(show_profile);
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        profileButton.setPressed(true);
        updatePicture = findViewById(R.id.btnUpdatePicture);

        tvName = findViewById(R.id.name);
        tvContact = findViewById(R.id.contact);
        tvHomepage = findViewById(R.id.homepage);
        switchGeolocation = findViewById(R.id.geoswitch);

        // Get the fcmToken of the Attendee
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        String fcmToken = prefs.getString("token", "missing token");
        Log.d("Firestore", String.format("TEST TOKEN STRING '%s'", fcmToken));
        // set the profile attribute fields
        setProfileFields(fcmToken);

        // Listener for the Geolocation tracking switch
        switchGeolocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Database database = new Database();
                // updates the Attendee's geolocation attribute
                database.updateAttendeeGeolocation(fcmToken, isChecked);
            }
        });


        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), EventListView.class);
                startActivity(event);
            }
        });

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(event);
            }
        });
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), createNewEventScreen1.class);
                startActivity(event);
            }
        });
        updatePicture.setOnClickListener(v -> {
            new updatePictureFragment().show(getSupportFragmentManager(), "Update Picture");
        });
    }

    // Sets the TextViews and Switch to the Attendee's Profile's attributes
    public void setProfileFields(String fcmToken){
        DocumentReference docRef = attendeesRef.document(fcmToken);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Attendee attendee = documentSnapshot.toObject(Attendee.class);
                if(attendee == null){
                    Log.e("Firestore", "Attendee doc not found");
                }
                else{
                    Log.d("Firestore", String.format("Attendee with name (%s) retrieved", attendee.getProfile().getName()));
                    Profile profile = attendee.getProfile();
                    // Construct strings, should probably restructure textviews so the field values are right-aligned
                    String nameText = getResources().getString(R.string.profile_name_text, profile.getName());
                    String contactText = getResources().getString(R.string.profile_contact_text, profile.getContact());
                    String homepageText = getResources().getString(R.string.profile_homepage_text, profile.getHomepage());
                    // set fields for profile
                    tvName.setText(nameText);
                    tvContact.setText(contactText);
                    tvHomepage.setText(homepageText);
                    switchGeolocation.setChecked(profile.getTrackGeolocation());
                }
            }
        });
    }
}