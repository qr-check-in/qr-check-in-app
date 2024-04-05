package com.example.qrcheckin.Common;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qrcheckin.Attendee.ProfileActivity;
import com.example.qrcheckin.R;
import com.example.qrcheckin.Admin.AdminActivity;
import com.example.qrcheckin.Admin.AdminTokensDatabaseManager;
import com.example.qrcheckin.Attendee.AttendeeDatabaseManager;
import com.example.qrcheckin.Event.CreateAddEventDetails;
import com.example.qrcheckin.Event.EventListView;

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
    Button scanButton;
    Button adminButton;
    private String fcmToken;
    private static final int REQUEST_CODE = 100;
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

        qrButton = findViewById(R.id.qrButton);
        qrButton.setPressed(true);

        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);
        scanButton = findViewById(R.id.scanButton);
        adminButton = findViewById(R.id.adminButton);
        adminButton.setVisibility(View.INVISIBLE);

        SharedPreferences tokenPrefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        fcmToken = tokenPrefs.getString("token", "missing token");
        Log.d("Firestore", String.format("your fcmToken is  (%s)", fcmToken));

        // Creates a sub applicaton. If app.hasCheckFcmToken is false, it means the app has just been opened
        OpenApp app = (OpenApp) this.getApplicationContext();
        if (!app.hasCheckedFcmToken){
            // Any methods called within this if statement only occur once upon the app's launch

            // checkExistingAttendees will store a new Attendee document if there's not already an existing doc for this fcmToken
            // in case an attendee doc has been deleted, but the app has not been deleting and reinstalled to create an new token
            AttendeeDatabaseManager db = new AttendeeDatabaseManager(fcmToken);
            db.checkExistingAttendees();

            app.hasCheckedFcmToken = true;
        }

        // Check if the admin dashboard button should be visible
        checkAdminToken();

        // Set the Header of the App
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("QRCheckIN");

        //Ask for permissions
        askNotificationPermission();


        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminView();
            }
        });
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
     * Checks if the provided FCM token exists in the 'adminTokens' collection in Firestore.
     * If the token exists, the user is considered an admin and the admin view is opened.
     * If the token does not exist, or an error occurs during the check, appropriate actions
     * or error handling can be implemented.
     *
     */
    private void checkAdminToken() {
        // Reference to the 'adminTokens' collection
        AdminTokensDatabaseManager adminTokensDb = new AdminTokensDatabaseManager(fcmToken);

        // Check if the current FCM token exists in the 'adminTokens' collection
        adminTokensDb.getDocRef().get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // The document exists, meaning this device is associated with an admin
                adminButton.setVisibility(View.VISIBLE);
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
    }

    //https://firebase.google.com/docs/cloud-messaging/android/client, 2024, how to enable permissions
    // Declare the launcher at the top of your Activity/Fragment:
    /**
     *
     */
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    Toast.makeText(getApplicationContext(), "Permission Granted", 1);
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Toast.makeText(getApplicationContext(), "Permission Denied", 1);
                }
            });

    //https://firebase.google.com/docs/cloud-messaging/android/client, 2024, how to enable permissions
    /**
     * 
     */
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}