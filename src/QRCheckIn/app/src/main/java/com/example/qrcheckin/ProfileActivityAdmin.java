package com.example.qrcheckin;

import static com.example.qrcheckin.R.layout.show_profile;
import static com.example.qrcheckin.R.layout.show_profile_admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Manages user profile view within the app.
 * User can view & edit their name, contact details, homepage, and profile picture.
 * Can toggle geolocation tracking .
 * Listens for updates from the EditProfileFragment dialog and updates the UI.
 */
public class ProfileActivityAdmin extends AppCompatActivity {
    Admin admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(show_profile_admin);

        String documentId = getIntent().getStringExtra("DOCUMENT_ID");

//        String name = getIntent().getStringExtra("name");
//        String contact = getIntent().getStringExtra("contact");
//        String homepage = getIntent().getStringExtra("homepage");

        // Find views and set data
        TextView nameTextView = findViewById(R.id.profile_name1);
        TextView contactTextView = findViewById(R.id.contact1);
        TextView homepageTextView = findViewById(R.id.homepage2);
        Button back = findViewById(R.id.btnBack);
        admin = new Admin();

        // Use the viewProfile method with the fetched document ID
        admin.viewProfile(documentId, new Admin.ProfileCallback() {
            @Override
            public void onProfileFetched(Map<String, Object> profile) {
                // Update UI with fetched profile data
                runOnUiThread(() -> {
                    nameTextView.setText((String) profile.get("name"));
                    contactTextView.setText((String) profile.get("contact"));
                    homepageTextView.setText((String) profile.get("homepage"));
                    // Update other UI elements as needed
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("ProfileActivityAdmin", "Error fetching profile", e);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), AdminViewProfiles.class);
                startActivity(event);
            }
        });
    }
}