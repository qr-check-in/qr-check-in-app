package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;


public class createNewEventScreen1 extends AppCompatActivity implements SelectDateFragment.DatePickerDialogListener, TimePickerFragment.TimePickerDialogListner{
    // Mainbar declarations
    ImageButton qrButton;
    ImageButton eventButton;
    ImageButton addEventButton;
    ImageButton profileButton;

    // Acitvity Widgets and text declarations
    Button nextPageButton;
    Button uploadPoster;
    Switch checkInSwitch;
    EditText eventNameEditText;
    EditText eventLocation;
    EditText eventDate;
    EditText eventTime;
    EditText eventDescription;
    ImageButton selectDateButton;
    ImageButton selectTimeButton;
    ImageView poster;
    TextView posterTempText;


    private String inputEventName;
    private String inputEventDate;
    private String inputEventTime;
    private String inputEventLocation;
    private boolean isChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event_screen_1);

        // Initialize Mainbar Attributes
        qrButton = findViewById(R.id.qrButton);
        eventButton = findViewById(R.id.calenderButton);
        addEventButton = findViewById(R.id.addCalenderButton);
        addEventButton.setPressed(true);
        profileButton = findViewById(R.id.profileButton);
        nextPageButton = findViewById(R.id.nextButton);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.addEventToolBar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Create an Event");

        // Initialize Event textbox and widgets
        uploadPoster = findViewById(R.id.uploadPosterButton);
        poster = findViewById(R.id.posterImageView);
        posterTempText = findViewById(R.id.posterTempText);
        checkInSwitch = findViewById(R.id.checkInSwitch);
        eventNameEditText = findViewById(R.id.eventNameText);
        eventLocation = findViewById(R.id.eventLocationText);
        eventDate = findViewById(R.id.eventDateText);
        eventTime = findViewById(R.id.eventTimeText);
        eventDescription = findViewById(R.id.eventDescriptionText);
        selectTimeButton = findViewById(R.id.eventTimePicker);
        selectDateButton = findViewById(R.id.eventDatePicker);


        poster.setVisibility(View.GONE);

        // Listener to show a DatePicker fragment when selectDateButton is clicked
        selectDateButton.setOnClickListener(v -> {
            new SelectDateFragment().show(getSupportFragmentManager(), "Select Date");
        });

        // Listener to show a TimePicker fragment when selectTimeButton is clicked
        selectTimeButton.setOnClickListener(v -> {
            new TimePickerFragment().show(getSupportFragmentManager(), "timePicker");
        });

        // Listener to go to event list page
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), EventListView.class);
                startActivity(event);
            }
        });

        // Listener to go to scanQR page
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(event);
            }
        });

        // Listener to add/upload a poster from gallery
        // https://developer.android.com/jetpack/androidx/releases/activity#1.7.0, 2024, how to select a picture from gallery
        uploadPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }

        });


        nextPageButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Retrieves input fields for the Event's attributes and passes them to createNewEvent2 activity
             * upon the nextPageButton being clicked
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Get the Event attributes from the input fields
                inputEventName = eventNameEditText.getText().toString();
                Intent event = new Intent(getApplicationContext(), createNewEventScreen2.class);
//                Instead of passing particular values, set the values to a new event and pass the event object, more convienent
                if (checkInSwitch.isChecked()){
                    isChecked = true;
                }else{isChecked = false;}
                Event newEvent = new Event(null, null, null,
                        inputEventName, inputEventDate, inputEventTime,
                        eventLocation.getText().toString(), eventDescription.getText().toString(), isChecked);
                // Store Event attributes to pass to createNewEventScreen2
                // https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application , 2011, user914425
//                event.putExtra("eventDate", inputEventDate);
                event.putExtra("newEvent", newEvent);
                //Log.d("event", String.format("going to pass %s %s", inputEventName, inputEventDate));
                startActivity(event);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), profileFragment.class);
                startActivity(event);
            }
        });

    }

    /**
     * Builds a string in the format "yyyy-mm-dd" and assigns it to inputEventDate
     * @param year Int of the year
     * @param month Int of the month (starts at 0)
     * @param dayOfMonth Int of the day
     */
    public void buildDate(int year, int month, int dayOfMonth){
        // https://stackoverflow.com/questions/6421874/how-to-get-the-date-from-the-datepicker-widget-in-android , 2011, rabby
        // Month is 0 based so add 1
        StringBuilder dateBuilder = new StringBuilder()
        .append(year).append("-")
                .append(month + 1).append("-")
                .append(dayOfMonth).append("");
        inputEventDate = dateBuilder.toString();

        // Set the textbox to the selected date
        eventDate.setText(inputEventDate);
    }

    public void buildTime(int hour, int minute){
        StringBuilder timeBuilder = new StringBuilder()
                .append(hour).append(":").append(minute);
        inputEventTime = timeBuilder.toString();

        eventTime.setText(inputEventTime);
    }

    // https://developer.android.com/jetpack/androidx/releases/activity#1.7.0, 2024, how to select a picture from gallery
    // Registers a photo picker activity launcher in single-select mode.
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    // Load the selected image into the ImageView using Glide
                    // openai, 2024, chatgpt, how to display the image
                    Glide.with(this)
                            .load(uri)
                            .into(poster);
                    poster.setVisibility(View.VISIBLE);
                    posterTempText.setVisibility(View.GONE);
                    EventPoster eventPoster = new EventPoster(uri, null);
                    eventPoster.uploadImage("/EventPosters", uri.toString());
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
}