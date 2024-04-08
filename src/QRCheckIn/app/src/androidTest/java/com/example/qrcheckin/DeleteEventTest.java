package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import static org.hamcrest.CoreMatchers.allOf;

import android.view.View;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.qrcheckin.Event.CreateAddEventDetails;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeleteEventTest {

    @Rule
    public ActivityScenarioRule<CreateAddEventDetails> scenario = new ActivityScenarioRule<CreateAddEventDetails>(CreateAddEventDetails.class);

    private CreateEventAndCheck createEventAndCheck;

    @Before
    public void setUp() {
        // Initialize Intents
        Intents.init();

        // setup for testing notifications
        // creates a event to push notifications
        createEventAndCheck = new CreateEventAndCheck();
        createEventAndCheck.testMatchPosterDetails();
    }

    @After
    public void tearDown() {
        // Release Intents
        Intents.release();
    }

    @Test
    public void testDeleteEvent() {

        // Check if the activity is being displayed is correct
        onView(withId(R.id.organizerEventPoster)).check(matches(isDisplayed()));

        // Open the bottomSheetButton by clicking on a button
        onView(withId(R.id.openBottomSheetButton)).perform(click());

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Open the deleteEvent by clicking on a button
        onView(withId(R.id.deleteEvent)).perform(click());

        // Perform a click on the "Delete" button
        onView(withText("DELETE")).perform(click());

        // Check if the activity is being displayed is correct
        onView(withId(R.id.eventListView)).check(matches(isDisplayed()));

        // Check if an event with the specific name exists
        String eventName = "ZZZ+ Annual Conference";
        onView(withText(eventName)).check(doesNotExist());

        // Click on upcomingEventsButton and event deleted
        onView(allOf(withId(R.id.upcomingEventsButton), isDescendantOfA(withId(R.id.eventsTabbar))))
                .perform(click());

        // Check if an event with the specific name exists
        onView(withText(eventName)).check(doesNotExist());

    }

}
