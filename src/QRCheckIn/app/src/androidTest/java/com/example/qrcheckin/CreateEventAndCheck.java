package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
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

import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateEventAndCheck {

    String title = "AAA Annual Conference";
    String detail = "'AAA Annual Conference' is a premier event gathering professionals and experts from the field of Computer Science";
    Calendar currentDate;
    Calendar time;

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

        // Test the text boxes
        onView(withId(R.id.eventNameText)).perform(ViewActions.typeText(title));

        // Open the SelectDateFragment by clicking on a button
        onView(withId(R.id.eventDateText)).perform(click());

        // Wait for the DatePickerDialog to be displayed
        onView(withClassName(Matchers.equalTo(DatePickerDialog.class.getName())));

        // Get the current date
        currentDate = Calendar.getInstance();

        // Add two days to the current date
        currentDate.add(Calendar.DAY_OF_MONTH, 2);

        // Click on the OK button of the DatePickerDialog to select the current date
        onView(withText("OK")).perform(click());

        // Open the TimePickerFragment by clicking on a button
        onView(withId(R.id.eventTimeText)).perform(click());

        // Wait for the TimePickerDialog to be displayed
        onView(withClassName(Matchers.equalTo(TimePickerDialog.class.getName())));

        // Use the current time as the default values for the picker.
        time = Calendar.getInstance();

        // Add two hours to the current time
        time.add(Calendar.HOUR_OF_DAY, 2);

        // Click on the OK button of the TimePickerDialog to select the time two hours from now
        onView(withText("OK")).perform(click());

        // Type the location
        onView(withId(R.id.eventLocationText)).perform(ViewActions.typeText("Toronto, Canada"));

        // Test the switch by enabling it
        onView(withId(R.id.checkInSwitch)).perform(click()).perform(ViewActions.closeSoftKeyboard());

        // Test the event size text box
        onView(withId(R.id.numOfAttendeeText)).perform(ViewActions.typeText("100"));

        // Close the keyboard
        onView(withId(R.id.eventDescriptionText)).perform(ViewActions.closeSoftKeyboard());

        // Scroll to the bottom of the page
        onView(withId(R.id.scrollView2)).perform(swipeUp());

        // Test event description
        onView(withId(R.id.eventDescriptionText)).perform(ViewActions.typeText(detail));

        // Perform action that triggers the new activity
        onView(withId(R.id.nextButton)).perform(click());

    }

    @Test
    public void testGenerateQrCode() {

        // First run this test to get event details
        testCreateEventInputFields();

        // Check that the intended activity is started
        intended(IntentMatchers.hasComponent(CreateGenerateEventQR.class.getName()));

        // perform click on btnGenCheckInQR to generate QR Code
        onView(withId(R.id.btnGenCheckInQR)).perform(click());

        // perform click on finishButton to create event
        onView(withId(R.id.finishButton)).perform(click());

    }

    @Test
    public void testSearchEventInList() {

        // First run this test to get better setup;
        testGenerateQrCode();

        // Now check in the events page if the created event exists or not

        // move to EventListView using intent
        intended(IntentMatchers.hasComponent(EventListView.class.getName()));

        // Click on the event with text stored in title
        onView(withText(title)).perform(click());

        //
//        onView(withId(withId(R.id.mainHeader)), isDescendantOfA(withId(R.id.event_page_toolbar))))
//                .check(matches(withText("Expected Text")));
    }
}
