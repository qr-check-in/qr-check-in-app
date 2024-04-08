package com.example.qrcheckin.Notifications;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qrcheckin.Event.EventDatabaseManager;
import com.example.qrcheckin.Event.OrganizersEventPageActivity;
import com.example.qrcheckin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateNotification extends AppCompatActivity {

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
    private String topicName;

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
        header.setText("");

        // find widgets and views
        notificationTitle = findViewById(R.id.notificationTitleText);
        notificationDescription = findViewById(R.id.notificationDescriptionText);
        addNotificationBtn = findViewById(R.id.addNotification);

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

                    // Retrieve the list of attendees from the event document
                    // openai, 2024, chatgpt, how to fetch a field from the db
                    eventDb.getCollectionRef().document(documentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                List<String> attendees = (List<String>) documentSnapshot.get("attendee");
                                if (attendees != null) {
                                    // Create JSONArray from attendees list
                                    JSONArray regArray = new JSONArray(attendees);
                                    // Print the contents of regArray
                                    Log.d("CreateNotification", "Attendees: " + regArray.toString());
                                    // Call createAnnoucement method with the list of attendees
                                    createAnnoucement(regArray);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("CreateNotification", "Error getting attendees: " + e.getMessage());
                        }
                    });

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

    /**
     * Server takes the data and sends the notification to attendees
     * @param regArray list of FCM tokens of the participants
     */
    public void createAnnoucement(JSONArray regArray){
        MyNotificationManager firebaseMessaging = new MyNotificationManager(getApplicationContext());
        firebaseMessaging.sendMessageToClient(regArray, notiTitle, notiDescription, documentId);
    }
}