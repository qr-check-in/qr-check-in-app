package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertTrue;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
//import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;


import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateEventAndCheck {
    @Rule
    public ActivityScenarioRule<CreateAddEventDetails> scenario = new ActivityScenarioRule<CreateAddEventDetails>(CreateAddEventDetails.class);

    private EventAdapter adapter;
    private RecyclerView recyclerView;
    public int rand = 2000 + (new Random().nextInt(1000));


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
    public void testButtonsAndTextboxes() {
        String title = "Happy Birthday Dear";

        // Test the text boxes
        onView(withId(R.id.eventNameText)).perform(ViewActions.typeText(title));
        onView(withId(R.id.eventDateText)).perform(ViewActions.typeText("2024-3-20"));
        onView(withId(R.id.eventTimeText)).perform(ViewActions.typeText("15:30"));
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
        onView(withId(R.id.eventDescriptionText)).perform(ViewActions.typeText("Welcome! Bless me with best wishes."));


        // Perform action that triggers the new activity
        onView(withId(R.id.nextButton)).perform(click());

        // A random delay (So, that it do too fast)
//        Thread.sleep(rand);

        // Check that the intended activity is started
        intended(IntentMatchers.hasComponent(CreateGenerateEventQR.class.getName()));

        // perform click on btnGenCheckInQR to generate QR Code
        onView(withId(R.id.btnGenCheckInQR)).perform(click());

        // perform click on finishButton to create event
        onView(withId(R.id.finishButton)).perform(click());

        // A random delay (So, that it do too fast)
//        Thread.sleep(rand);


        // Now check in the events page if the created event exists or not

        // move to EventListView using intend
        intended(IntentMatchers.hasComponent(EventListView.class.getName()));

        // click on My Events to see events created by user (YOU)
        ViewInteraction myEventsView = onView(allOf(withId(R.id.myEventsButton), isDescendantOfA(withId(R.id.eventsTabbar))));
        myEventsView.perform(click());

        // Delay to ensure the view is loaded
//        Thread.sleep(1000);

        // Find the position of the event with the name containing "Happy"
        int position = findEventPosition(title);

        // Perform action on the item at the obtained position
        Log.d("Position", "event position: " + position);
        assertTrue("Event with title containing '" + title + "' not found.", position != -1);

        // Perform action on the item at the obtained position
        Log.d("Position", "event position: " + position);

        // Scroll to and click on the event
//        onView(withId(R.id.event_recycler_view)).perform(actionOnItemAtPosition(position, click()));
//        onView(withText(title)).perform(scrollTo(), click());



    }


    private int findEventPosition(String title) {
        ActivityScenario<EventListView> scenario = ActivityScenario.launch(EventListView.class);
        int[] position = {-1}; // Using an array to store the position since it needs to be final or effectively final in the lambda expression
        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.event_recycler_view);
            if (recyclerView != null) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter instanceof EventAdapter) {
                    EventAdapter eventAdapter = (EventAdapter) adapter;
                    Log.d("EventAdapter", "Item Count: " + eventAdapter.getItemCount());
                    for (int i = 0; i < eventAdapter.getItemCount(); i++) {
                        Event event = eventAdapter.getItem(i);
                        Log.d("EventName", "EventName[" + i + "] : " + event.getEventName());
                        if (event != null && event.getEventName().contains(title)) {
                            position[0] = i;
                            break;
                        }
                    }
                }
            }
        });
        scenario.close(); // Close the activity after performing the actions
        return position[0];
    }

}
