package com.example.qrcheckin;

import static com.example.qrcheckin.R.layout.show_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(show_profile);

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
            Picasso.get().load(uri).into(profileImageView);
        });

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
}