package com.example.qrcheckin;

import static com.example.qrcheckin.R.layout.show_profile;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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

public class profileFragment extends AppCompatActivity implements editProfilefragment.EditProfileDialogListener {
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
            new updatePictureFragment(fcmToken).show(getSupportFragmentManager(), "Update Picture");
        });
        removePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the CircleImageView to show the default profile image
                profileImageView.setImageResource(R.drawable.profile); // Assuming 'profile' is your default/placeholder image

                // If you have logic that stores the current profile image (e.g., in Shared Preferences or a database),
                // ensure to update that as well to reflect the removal/resetting of the profile picture.
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
                editProfilefragment fragment = new editProfilefragment();

                // Set the bundle as arguments for the fragment
                fragment.setArguments(bundle);

                // Show the fragment
                fragment.show(getSupportFragmentManager(), "Edit Profile");
            }
        });

    }

    @Override
    public void editDetails(String nameUpdated, String contactUpdated, String homepageUpdated) {
        name = nameUpdated;
        contact = contactUpdated;
        homepage = homepageUpdated;
        userNameBesidePic.setText(nameUpdated);
        userName.setText(nameUpdated);
        userContact.setText(contactUpdated);
        userHomepage.setText(homepageUpdated);

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
                    // set fields for profile
                    tvName.setText(profile.getName());
                    tvContact.setText(profile.getContact());
                    tvHomepage.setText(profile.getHomepage());
                    switchGeolocation.setChecked(profile.getTrackGeolocation());
                    displayProfilePicture(profile.getPicture());
                }
            }
        });
    }

    public void displayProfilePicture(String uriString){
        String filePath = "/content:/media/picker/0/com.android.providers.media.photopicker/media/1000000037";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        // ISSUE: file name is not getting saved as its uri string, so we can't find the file in storage
        storageReference = FirebaseStorage.getInstance().getReference().child(filePath);
        try{
            final File localFile = File.createTempFile("tempProfilePic", "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Firestore", "picture retrieved");
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