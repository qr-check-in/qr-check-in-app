package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.qrcheckin.Event.CreateAddEventDetails;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotificationTest {

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
    public void testNotification() {
        String notificationTitle = "First Milestone Achievement!";
        String notificationDescription = "Attendee count reached 100.";

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

        // Open the createEventNotification by clicking on a button
        onView(withId(R.id.createEventNotification)).perform(click());

        // Check if the activity is being displayed is correct
        onView(withId(R.id.createNotification)).check(matches(isDisplayed()));

        // Input text in Notification Title text field
        onView(withId(R.id.notificationTitleText)).perform(ViewActions.typeText(notificationTitle), ViewActions.closeSoftKeyboard());

        // Input text in Notification Description text field
        onView(withId(R.id.notificationDescriptionText)).perform(ViewActions.typeText(notificationDescription), ViewActions.closeSoftKeyboard());

        // Click on Post Announcement button to sent notification to all attendees
        onView(withId(R.id.addNotification)).perform(click());

        // Check if the activity is being displayed is correct
        onView(withId(R.id.organizerEventPoster)).check(matches(isDisplayed()));

        // Click the Notification Icon button to pop up a notifications
        onView(allOf(withId(R.id.notificationIconBtn), isDescendantOfA(withId(R.id.organizer_eventScreen_toolbar)))).perform(click());

        // Check if the correct dialog window is displayed
        onView(withText(R.string.event_annoucements))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        // Check if the Notification Title is displayed
        onView(withText(notificationTitle)).check(matches(isDisplayed()));

        // Check if the Notification Description is displayed
        onView(withText(notificationTitle)).check(matches(isDisplayed()));

    }

}
