package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.util.Log;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.qrcheckin.Event.CreateAddEventDetails;
import com.example.qrcheckin.Event.CreateGenerateEventQR;
import com.example.qrcheckin.Event.EventListView;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateEventAndCheck {

    String title = "ZZZ+ Annual Conference";
    String detail = "'ZZZ+ Annual Conference' is a premier event gathering professionals and experts from the field of Computer Science";
    Calendar currentDate;
    Calendar time;
    String location = "Toronto, Canada";

    @Rule
    public ActivityScenarioRule<CreateAddEventDetails> scenario = new ActivityScenarioRule<CreateAddEventDetails>(CreateAddEventDetails.class);


    @Before
    public void setUp() {
        // Initialize Intents
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release Intents
        Intents.release();
    }

    /**
     * Tests the text boxes and buttons on the page
     * Create a Event and generate QR Code for check-in
     * Finally, check if the event exists
     */
    @Test
    public void testCreateEventInputFields() {

        // Check if the activity is being displayed is correct
        onView(withId(R.id.createAddEventDetails)).check(matches(isDisplayed()));

        // Test the text boxes
        onView(withId(R.id.eventNameText)).perform(ViewActions.typeText(title), ViewActions.closeSoftKeyboard());

        // Open the SelectDateFragment by clicking on a button
        onView(withId(R.id.eventDateText)).perform(click());

        // Wait for the DatePickerDialog to be displayed
        onView(withClassName(Matchers.equalTo(DatePickerDialog.class.getName())));

        // Get the current date
        currentDate = Calendar.getInstance();

        // Click on the OK button of the DatePickerDialog to select the current date
        onView(withText("OK")).perform(click());

        // Open the TimePickerFragment by clicking on a button
        onView(withId(R.id.eventTimeText)).perform(click());

        // Wait for the TimePickerDialog to be displayed
        onView(withClassName(Matchers.equalTo(TimePickerDialog.class.getName())));

        // Use the current time as the default values for the picker.
        time = Calendar.getInstance();

        // Click on the OK button of the TimePickerDialog to select the time two hours from now
        onView(withText("OK")).perform(click());

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Type the location
        onView(withId(R.id.eventLocationText)).perform(ViewActions.typeText(location), ViewActions.closeSoftKeyboard());

        // Test the switch by enabling it
        onView(withId(R.id.checkInSwitch)).perform(click()).perform(ViewActions.closeSoftKeyboard());

        // Test the event size text box
        onView(withId(R.id.numOfAttendeeText)).perform(ViewActions.typeText("100"), ViewActions.closeSoftKeyboard());

        // Scroll to the bottom of the page
        onView(withId(R.id.scrollView2)).perform(swipeUp());

        // Test event description
        onView(withId(R.id.eventDescriptionText)).perform(ViewActions.typeText(detail), ViewActions.closeSoftKeyboard());

        // Perform action that triggers the new activity
        onView(withId(R.id.nextButton)).perform(click());

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGenerateQrCode() {

        // First run this test to get event details
        testCreateEventInputFields();

        // Check that the intended activity is started
        intended(IntentMatchers.hasComponent(CreateGenerateEventQR.class.getName()));

        // Check if the activity is being displayed
        onView(withId(R.id.genertaeQrCodeActivity)).check(matches(isDisplayed()));

        // perform click on btnGenCheckInQR to generate QR Code
        onView(withId(R.id.btnGenCheckInQR)).perform(click());

        // perform click on finishButton to create event
        onView(withId(R.id.finishButton)).perform(click());

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSearchEventInList() {

        // First run this test to get better setup;
        testGenerateQrCode();

        // Now check in the events page if the created event exists or not

        // move to EventListView using intent
        intended(IntentMatchers.hasComponent(EventListView.class.getName()));

        // Check if the activity is being displayed
        onView(withId(R.id.eventListView)).check(matches(isDisplayed()));

        // press myEventsButton to see events created by user
        onView(withId(R.id.eventsTabbar)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.myEventsButton))
                .check(matches(isDisplayed())).perform(click());

        // Click on the event with text stored in title
        onView(withText(title)).perform(click());


    }

    @Test
    public void testMatchPosterDetails() {

        // First run this test to get event Poster
        testSearchEventInList();

        // match details on poster with input details while creating event

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());

        // Format the currentDate using the SimpleDateFormat object
        String formattedDate = dateFormat.format(currentDate.getTime());

        Log.d("CurrentDate", "testMatchPosterDetails: " + formattedDate);

        // Match event date with the converted string
        onView(withId(R.id.text_event_date)).check(matches(withText(formattedDate)));

        // Match event location
        onView(withId(R.id.text_event_location)).check(matches(withText(location)));

        // Match event description
        onView(withId(R.id.text_event_description)).check(matches(withText(detail)));

    }
}
