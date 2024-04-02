package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNotification extends AppCompatActivity {
    // Main Bar buttons
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;

    // Widgets and text on the activity
    TextView notificationTitle;
    TextView notificationDescription;
    Button addNotificationBtn;

    String notiTitle;
    String notiDescription;
    String notiDateTime;
    private NotificationDatabaseManager notiDb;
    private EventDatabaseManager eventDb;
    private String fcmToken;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_notification);

        // Manage Toolbar
        Toolbar toolbar = findViewById(R.id.notificationToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Create New Annoucement");

        // find widgets and views
        notificationTitle = findViewById(R.id.notificationTitleText);
        notificationDescription = findViewById(R.id.notificationDescriptionText);
        addNotificationBtn = findViewById(R.id.addNotification);

        // Main Bar Widgets
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        profileButton = findViewById(R.id.profileButton);

        // Set up listeners for main app toolbar buttons
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Please Finish Creating an Annoucement", Toast.LENGTH_LONG).show();
            }
        });
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Please Finish Creating an Annoucement", Toast.LENGTH_LONG).show();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Please Finish Creating an Annoucement", Toast.LENGTH_LONG).show();
            }
        });

        // Retrieve the event passed from the previous activity
        documentId = getIntent().getStringExtra("EVENT_DOC_ID");
        eventDb = new EventDatabaseManager(documentId);
        notiDb = new NotificationDatabaseManager();

        // Inside the addNotificationBtn.setOnClickListener method of CreateNotification activity
        // openai, 2024,  chatgpt: how to go back to the previous event
        addNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the notification title and description from EditText fields
                notiTitle = notificationTitle.getText().toString();
                notiDescription = notificationDescription.getText().toString();

                // Check if either title or description is empty
                if (TextUtils.isEmpty(notiTitle) || TextUtils.isEmpty(notiDescription)) {
                    Toast.makeText(getApplicationContext(), "Please fill in both title and description", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform notification creation logic
                    // openai,2024, chatgpt how to get the date and time
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM, dd, yyyy; h:mm a", Locale.getDefault());
                    notiDateTime = dateFormat.format(new Date());

                    // Create new object notification and add it to the db
                    Notification newNotification = new Notification(notiTitle, notiDescription, notiDateTime,documentId);
                    Log.d("notification", String.format("storing Notification%s", newNotification.getTitle()));
                    String notiID = notiDb.storeNotification(newNotification);

                    // Add to eventdb
                    eventDb.addToArrayField("notifications", notiID);


                    // Once the notification is created, navigate back to OrganizersEventPageActivity
                    Intent intent = new Intent(getApplicationContext(), OrganizersEventPageActivity.class);
                    intent.putExtra("DOCUMENT_ID", documentId);
                    startActivity(intent);
                }
            }
        });
    }

    //openai, 2024, chatgpt: how to pass the document Id through the upButton
    /**
     * Pass information through the the up button
     * @param item The menu item that was selected.
     *
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate back to OrganizersEventPageActivity with documentId
                Intent intent = new Intent(getApplicationContext(), OrganizersEventPageActivity.class);
                intent.putExtra("DOCUMENT_ID", documentId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}