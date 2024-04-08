package com.example.qrcheckin.Admin;

import static com.example.qrcheckin.R.layout.show_profile_admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.qrcheckin.Common.Image;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrcheckin.Attendee.Attendee;
import com.example.qrcheckin.Attendee.AttendeeDatabaseManager;
import com.example.qrcheckin.Attendee.EditProfileFragment;
import com.example.qrcheckin.Attendee.Profile;
import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.Common.SharedViewModel;
import com.example.qrcheckin.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

/**
 * Manages user profile view within the app.
 * User can view & edit their name, contact details, homepage, and profile picture.
 * Can toggle geolocation tracking .
 * Listens for updates from the EditProfileFragment dialog and updates the UI.
 */
public class AdminProfileActivity extends AppCompatActivity {
    Admin admin;
    ImageView editProfile;
    private SharedViewModel sharedViewModel;
    private ImageView profileImageView;
    private String name = "";
    private String contact = "";
    private String homepage = "";
    private AttendeeDatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(show_profile_admin);

        String documentId = getIntent().getStringExtra("DOCUMENT_ID");
        editProfile = findViewById(R.id.edit_profile);
        editProfile.setVisibility(View.GONE);
//        String name = getIntent().getStringExtra("name");
//        String contact = getIntent().getStringExtra("contact");
//        String homepage = getIntent().getStringExtra("homepage");
        profileImageView = findViewById(R.id.profile_image);

        // Find views and set data
        TextView nameTextView = findViewById(R.id.profileName);
        TextView nameTextView2 = findViewById(R.id.profileName1);
        TextView contactTextView = findViewById(R.id.contact1);
        TextView homepageTextView = findViewById(R.id.homepage1);
        Button removeProfile = findViewById(R.id.btnRemoveProfile);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Profile");

        admin = new Admin();
        dbManager = new AttendeeDatabaseManager(documentId);
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
                deleteProfilePicture();
                admin.deleteProfile(documentId);
                Intent event = new Intent(getApplicationContext(), AdminViewProfiles.class);
                startActivity(event);
            }
        });

        //Use the viewProfile method with the fetched document ID
        admin.viewProfile(documentId, new Admin.ProfileCallback() {
            @Override
            public void onProfileFetched(Map<String, Object> profile, String profilePic) {
                // Update UI with fetched profile data
                if (profile != null && profilePic != null) {
                    runOnUiThread(() -> {
                        nameTextView.setText((String) profile.get("name"));
                        nameTextView2.setText((String) profile.get("name"));
                        contactTextView.setText((String) profile.get("contact"));
                        homepageTextView.setText((String) profile.get("homepage"));
                        if (profilePic != null) {
                            Log.d("profilePic1", "profilepic is:" + profilePic);
                            ImageStorageManager storage = new ImageStorageManager(new Image(profilePic, null), "/ProfilePictures");
                            storage.displayImage(profileImageView);
                        }
                    });
                } else {
                    nameTextView.setText((String) profile.get("name"));
                    nameTextView2.setText((String) profile.get("name"));
                    contactTextView.setText((String) profile.get("contact"));
                    homepageTextView.setText((String) profile.get("homepage"));
                }

            }

            @Override
            public void onError(Exception e) {
                Log.e("ProfileActivityAdmin", "Error fetching profile", e);
            }
        });

    }

    public void deleteProfilePicture() {
        dbManager.getDocRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Attendee attendee = documentSnapshot.toObject(Attendee.class);
                if (attendee == null) {
                    Log.e("Firestore", "Attendee doc not found");
                } else {
                    Profile profile = attendee.getProfile();
                    if (profile.getProfilePicture() != null) {
                        ImageStorageManager storage = new ImageStorageManager(profile.getProfilePicture(), "/ProfilePictures");
                        // Remove profile picture from storage
                        storage.deleteImage();
                        // update attendee doc's field
                        dbManager.updateProfilePicture(null);
                    }
                }
            }
        });

    }
}