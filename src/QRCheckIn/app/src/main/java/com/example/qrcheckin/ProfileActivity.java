package com.example.qrcheckin;

import static com.example.qrcheckin.R.layout.show_profile;

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

/**
 * Manages user profile view within the app.
 * User can view & edit their name, contact details, homepage, and profile picture.
 * Can toggle geolocation tracking .
 * Listens for updates from the EditProfileFragment dialog and updates the UI.
 */
public class ProfileActivity extends AppCompatActivity implements EditProfileFragment.EditProfileDialogListener {
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    private TextView userName;
    private TextView userContact;
    private TextView userHomepage;
    private TextView userNameBesidePic;

    Button updatePicture;
    Button removePicture;
    private ImageView profileImageView;
    private SharedViewModel sharedViewModel;
    private String name = "";
    private String contact = "";
    private String homepage = "";

    ImageView editProfile;

    TextView tvName;
    TextView tvContact;
    TextView tvHomepage;
    Switch switchGeolocation;
    TextView profileName;
    private AttendeeDatabaseManager dbManager;
    private String fcmToken;

    /**
     * init profile activity sets UI components & loading user profile data.
     * @param savedInstanceState If activity re-initialized after previously being shut down,
     *                           most recent data saved.
     *                           Otherwise, it is null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(show_profile);
        // Initialize navigation bar and other views
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        profileButton.setPressed(true);
        updatePicture = findViewById(R.id.btnUpdatePicture);
        removePicture = findViewById(R.id.btnRemovePicture);
        editProfile = findViewById(R.id.edit_profile);
        updatePicture = findViewById(R.id.btnUpdatePicture);
        userName = findViewById(R.id.edit_name);
        userContact = findViewById(R.id.edit_contact);
        userHomepage = findViewById(R.id.edit_homepage);
        userNameBesidePic = findViewById(R.id.profileName);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getSelectedImageUri().observe(this, new androidx.lifecycle.Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                profileImageView.setImageURI(uri);
            }
        });
        profileImageView = findViewById(R.id.profile_image);

        tvName = findViewById(R.id.profileName1);
        tvContact = findViewById(R.id.contact1);
        tvHomepage = findViewById(R.id.homepage1);
        switchGeolocation = findViewById(R.id.geoswitch);
        profileName = findViewById(R.id.profileName);

        // Get the fcmToken of the Attendee, initialize an AttendeeDatabaseManager
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        fcmToken = prefs.getString("token", "missing token");
        dbManager = new AttendeeDatabaseManager(fcmToken);

        // set the profile attribute fields and string attributes like name, contact, homepage
        setProfileFields();

        // Listener for the Geolocation tracking switch
        switchGeolocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // updates the Attendee's geolocation attribute
                dbManager.updateAttendeeGeolocation(isChecked);
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
                Intent event = new Intent(getApplicationContext(), CreateNewEventScreen1.class);
                startActivity(event);
            }
        });
        updatePicture.setOnClickListener(v -> {
            new UpdatePictureFragment(fcmToken).show(getSupportFragmentManager(), "Update Picture");
        });
        removePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.updateProfilePicture(null);
                profileImageView.setImageResource(R.drawable.profile);
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
    /**
     * Callback when profile details are updated via EditProfileFragment.
     * Updates the displayed profile info.
     *
     * @param nameUpdated The updated name of the user.
     * @param contactUpdated The updated contact information of the user.
     * @param homepageUpdated The updated homepage URL of the user.
     */
    @Override
    public void editDetails(String nameUpdated, String contactUpdated, String homepageUpdated) {
        dbManager.updateProfileString("name", nameUpdated);
        dbManager.updateProfileString("contact", contactUpdated);
        dbManager.updateProfileString("homepage", homepageUpdated);
        setProfileFields();

    }
    /**
     * Fetches user profile details from Firestore & updates the UI.
     */
    public void setProfileFields() {
        dbManager.getAttendeeDocRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Attendee attendee = documentSnapshot.toObject(Attendee.class);
                if (attendee == null) {
                    Log.e("Firestore", "Attendee doc not found");
                } else {
                    Log.d("Firestore",
                            String.format("Attendee with name (%s) retrieved", attendee.getProfile().getName()));
                    Profile profile = attendee.getProfile();
                    // sets strings for profile fragment
                    name = profile.getName();
                    contact = profile.getContact();
                    homepage = profile.getHomepage();
                    // set fields for profile
                    tvName.setText(name);
                    profileName.setText(name);
                    tvContact.setText(contact);
                    tvHomepage.setText(homepage);
                    switchGeolocation.setChecked(profile.getTrackGeolocation());
                    // Display profilePicutre if the profile has one
                    if(profile.getProfilePicture() != null){
                        Log.d("Firestore", "calling display profile pic");
                        ImageStorageManager storage = new ImageStorageManager();
                        storage.displayImage(profile.getProfilePicture(),"/EventPosters/", profileImageView);
                    }
                }
            }
        });
    }
}