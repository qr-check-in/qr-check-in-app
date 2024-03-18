package com.example.qrcheckin;

import static com.example.qrcheckin.R.layout.show_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference attendeesRef = db.collection("Attendees");
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

        sharedViewModel.getSelectedImageUri().observe(this, uri -> {
            // Use Picasso, Glide, or similar library to load the image efficiently
            // Picasso.get().load(uri).into(profileImageView);
        });

        tvName = findViewById(R.id.profileName1);
        tvContact = findViewById(R.id.contact1);
        tvHomepage = findViewById(R.id.homepage1);
        switchGeolocation = findViewById(R.id.geoswitch);
        profileName = findViewById(R.id.profileName);

        // Get the fcmToken of the Attendee
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        fcmToken = prefs.getString("token", "missing token");
        Log.d("Firestore", String.format("TEST TOKEN STRING '%s'", fcmToken));
        // set the profile attribute fields and string attributes like name, contact, homepage
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
                DocumentReference userProfileRef = attendeesRef.document(fcmToken);

                // Directly set uriString to null without fetching the document first
                userProfileRef.update("profile.profilePicture.uriString", null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("ProfileFragment", "Profile picture URI set to null successfully.");
                                // Update UI with default profile image
                                profileImageView.setImageResource(R.drawable.profile); // Adjust with your default image
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ProfileFragment", "Failed to set profile picture URI to null.", e);
                            }
                        });
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
        Database database = new Database();
        database.updateProfileString(fcmToken, "name", nameUpdated);
        database.updateProfileString(fcmToken, "contact", contactUpdated);
        database.updateProfileString(fcmToken, "homepage", homepageUpdated);
        setProfileFields(fcmToken);

    }
    /**
     * Fetches user profile details from Firestore & updates the UI.
     * @param fcmToken used to identify the user's document in Firestore.
     */
    public void setProfileFields(String fcmToken) {
        DocumentReference docRef = attendeesRef.document(fcmToken);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                        displayProfilePicture(profile.getProfilePicture().getUriString());
                    }
                }
            }
        });
    }

    /**
     * Retrieves a file from FireStorage, converts to a bitmap and sets the profileImageView to display it
     * @param uriString name of the file in FireStorage
     */
    public void displayProfilePicture(String uriString){
        // Create string of the path to the image file in firestorage
        String filePath = "/ProfilePictures/"+uriString;
        // Get the file
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child(filePath);
        try{
            final File localFile = File.createTempFile("tempProfilePic", "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Firestore", "picture retrieved");
                    // Convert local file to bitmap and set the imageview
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profileImageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Firestore", "picture error");
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}