package com.example.qrcheckin;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Entry point of the app, hosts main interface.
 * Provides buttons for; scanning QR codes, viewing event list, adding a new event, accessing the user profile.
 * It initializes fetching and storing the FCM token.
 */
public class MainActivity extends AppCompatActivity{
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;
    private String fcmToken;
    Button scanButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference eventsRef = db.collection("events");

    /**
     * Sets up UI and initializes application settings.
     * Sets up toolbar buttons & one-time setup of fetching the FCM token.
     *
     * @param savedInstanceState If activity is re-initialized after previously being shut down, contains
     *                          most recent data.
     *                          Otherwise, it is null.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        qrButton = findViewById(R.id.qrButton);
        qrButton.setPressed(true);

        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        scanButton = findViewById(R.id.scanButton);

        // Creates a sub applicaton. If app.hasCheckFcmToken is false, it means the app has just been opened
        OpenApp app = (OpenApp) this.getApplicationContext();
        if (!app.hasCheckedFcmToken){
            // here we can call any methods we only want to occur once upon opening the app
            // Get and store this app installation's fcm token string
            // https://stackoverflow.com/questions/51834864/how-to-save-a-fcm-token-in-android , 2018, Whats Going On

            SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
            fcmToken = prefs.getString("token", "missing token");
            if ("missing token".equals(fcmToken)) {
                // If there is no token saved, retrieve it and save it
                SharedPreferences.Editor editor = prefs.edit();
                getFcmToken(editor);
            } else {
                // If the token is found, check for admin privileges
                checkAdminToken(fcmToken);
            }


            SharedPreferences.Editor editor = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE).edit();
            getFcmToken(editor);
            fcmToken = prefs.getString("token", "missing token");

            // Check if an Attendee object associated with this fcm token already exists
            AttendeeDatabaseManager db = new AttendeeDatabaseManager(fcmToken);
            db.checkExistingAttendees();
            Log.d("Firestore", String.format("fcmToken STRING (%s) stored", fcmToken));
            app.hasCheckedFcmToken = true;
        }

//        Set the Header of the App
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("QRCheckIN");


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), QRCodeScan.class);
                startActivity(event);
            }
        });

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), QRCodeScan.class);
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

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), CreateAddEventDetails.class);
                startActivity(event);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(event);

            }
        });
    }

    /**
     * Retrieves and logs the Firebase Cloud Messaging (FCM) token for this app's installation
     * @param editor a SharedPreferences.Editor from the calling activity to save the token string value
     */
    public void getFcmToken(SharedPreferences.Editor editor) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(Utils.TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get and log the new FCM registration token
                    String token = task.getResult();
                    Log.d(Utils.TAG, token);
                    // save token string
                    editor.putString("token", token);
                    editor.apply();
                });
    }
    /**
     * Checks if the provided FCM token exists in the 'adminTokens' collection in Firestore.
     * If the token exists, the user is considered an admin and the admin view is opened.
     * If the token does not exist, or an error occurs during the check, appropriate actions
     * or error handling can be implemented.
     *
     * @param token The FCM token to check against the 'adminTokens' collection.
     */
    private void checkAdminToken(String token) {
        // Reference to the 'adminTokens' collection
        CollectionReference adminTokensRef = db.collection("adminTokens");

        // Check if the current FCM token exists in the 'adminTokens' collection
        adminTokensRef.document(token).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // The document exists, meaning this device is associated with an admin
                openAdminView();
            } else {
                // The document does not exist, meaning this device is not associated with an admin
                // Handle accordingly, perhaps by staying on the current activity or opening a user view
            }
        }).addOnFailureListener(e -> {
            // Handle any errors here
            Log.e("Firestore", "Error checking admin token", e);
        });
    }

    /**
     * Opens the AdminActivity view. This method is typically called when a user has been
     * confirmed to have admin privileges.
     */
    private void openAdminView() {
        // Open the AdminActivity
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish(); // Close the current activity if necessary
    }

}