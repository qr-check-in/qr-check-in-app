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
import androidx.appcompat.widget.Toolbar;
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
    ImageView editProfile;
    private SharedViewModel sharedViewModel;
    private ImageView profileImageView;
    private String name = "";
    private String contact = "";
    private String homepage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(show_profile_admin);

        String documentId = getIntent().getStringExtra("DOCUMENT_ID");
        editProfile = findViewById(R.id.edit_profile);
//        String name = getIntent().getStringExtra("name");
//        String contact = getIntent().getStringExtra("contact");
//        String homepage = getIntent().getStringExtra("homepage");
        profileImageView = findViewById(R.id.profile_image);
        // Find views and set data
        TextView nameTextView = findViewById(R.id.profileName);
        TextView nameTextView2 = findViewById(R.id.profileName1);
        TextView contactTextView = findViewById(R.id.contact1);
        TextView homepageTextView = findViewById(R.id.homepage1);
        Button back = findViewById(R.id.btnBack);
        Button removeProfile = findViewById(R.id.btnRemoveProfile);
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Profile");
        admin = new Admin();
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getSelectedImageUri().observe(this, new androidx.lifecycle.Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                profileImageView.setImageURI(uri);
            }
        });
        removeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.deleteProfile(documentId);
                Intent event = new Intent(getApplicationContext(), AdminViewProfiles.class);
                startActivity(event);
            }
        });

        // Use the viewProfile method with the fetched document ID
        admin.viewProfile(documentId, new Admin.ProfileCallback() {
            @Override
            public void onProfileFetched(Map<String, Object> profile) {
                // Update UI with fetched profile data
                runOnUiThread(() -> {
                    nameTextView.setText((String) profile.get("name"));
                    nameTextView2.setText((String) profile.get("name"));
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
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new Bundle to hold the data
                Bundle bundle = new Bundle();

                // Put different types of data into the bundle
                bundle.putString("name", name);
                bundle.putString("contact", contact);
                bundle.putString("homepage", homepage);

                // Create a new instance of AddCityFragment
                EditProfileFragment fragment = new EditProfileFragment();

                // Set the bundle as arguments for the fragment
                fragment.setArguments(bundle);

                // Show the fragment
                fragment.show(getSupportFragmentManager(), "Edit Profile");
            }
        });

    }
}