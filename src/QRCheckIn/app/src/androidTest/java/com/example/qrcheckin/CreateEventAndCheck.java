package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateEventAndCheck {
    @Rule
    public ActivityScenarioRule<CreateAddEventDetails> scenario = new ActivityScenarioRule<CreateAddEventDetails>(CreateAddEventDetails.class);

    private EventAdapter adapter;
    private RecyclerView recyclerView;


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
    public void testButtonsAndTextboxes(){
        String title = "Happy Birthday Dear";

        // Test the text boxes
        onView(withId(R.id.eventNameText)).perform(ViewActions.typeText(title));
        onView(withId(R.id.eventDateText)).perform(ViewActions.typeText("2024-3-20"));
        onView(withId(R.id.eventTimeText)).perform(ViewActions.typeText("15:30"));
        onView(withId(R.id.eventLocationText)).perform(ViewActions.typeText("Toronto, Canada"));

        // Close the keyboard
        onView(withId(R.id.eventDescriptionText)).perform(ViewActions.closeSoftKeyboard());

        // Test event description
        onView(withId(R.id.eventDescriptionText)).perform(ViewActions.typeText("Testing description"));

        // Test the switch
        onView(withId(R.id.checkInSwitch)).perform(click());

        // Perform action that triggers the new activity
        onView(withId(R.id.nextButton)).perform(click());

        // Check that the intended activity is started
        intended(IntentMatchers.hasComponent(CreateGenerateEventQR.class.getName()));

        // perform click on btnGenCheckInQR to generate QR Code
        onView(withId(R.id.btnGenCheckInQR)).perform(click());

        // perform click on finishButton to create event
        onView(withId(R.id.finishButton)).perform(click());

        // Now check in the events page if the created event exists or not

        // move to EventListView using intend
        intended(IntentMatchers.hasComponent(EventListView.class.getName()));

        // Access the RecyclerView
        onView(withId(R.id.event_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click())); // Click on the item at position 0



    }
}
