package com.example.qrcheckin;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;

import android.view.View;
import android.widget.ImageButton;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
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
public class ParticipantAndSignUpTest {

    @Rule
    public ActivityScenarioRule<CreateAddEventDetails> scenario = new ActivityScenarioRule<CreateAddEventDetails>(CreateAddEventDetails.class);

    private CreateEventAndCheck createEventAndCheck;
    private String userName = "Morgan";

    @Before
    public void setUp() {
        // Initialize Intents
        Intents.init();

        // setup for testing notifications
        // creates a event to push notifications
        createEventAndCheck = new CreateEventAndCheck();
        createEventAndCheck.testMatchPosterDetails();

        // Change the profile name to "Morgan" so that it would be easy to verify at the time of signup

        // Click on profile button
        onView(withId(R.id.profileButton)).perform(click());

        // Click the edit button to pop up a fragment
        onView(allOf(withId(R.id.edit_profile), isDescendantOfA(withId(R.id.profileToolbar)))).perform(click());

        // Update the user name
        onView(withId(R.id.edit_name)).perform(ViewActions.clearText(), ViewActions.typeText(userName));

        // Perform a click action on the "Edit" button
        onView(withText("EDIT")).perform(click());

        // Get back to event poster page
        pressBack();

    }

    @After
    public void tearDown() {
        // Release Intents
        Intents.release();
    }

    @Test
    public void testSignUps() {

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

        // Open the viewSignedUp by clicking on a button
        onView(withId(R.id.viewSignedUp)).perform(click());

        // Check if the activity is being displayed is correct
        onView(withId(R.id.attendeeListView)).check(matches(isDisplayed()));

        // Check there should be zero signups as the event is just created (should be 0)
        onView(allOf(withId(R.id.mainHeader), isDescendantOfA(withId(R.id.attendee_toolbar))))
                .check(matches(withText("Total Participants: 1")));

        // Get back to event poster page
        pressBack();

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Scroll up to locate switch
        onView(withId(R.id.scrollOrganizerPage)).perform(swipeUp());

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Open the viewSignedUp by clicking on a button
        onView(withId(R.id.signup_button)).perform(click());


        // Open the bottomSheetButton by clicking on a button
        onView(withId(R.id.openBottomSheetButton)).perform(click());

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Open the viewSignedUp by clicking on a button
        onView(withId(R.id.viewSignedUp)).perform(click());

        // Check if the activity is being displayed is correct
        onView(withId(R.id.attendeeListView)).check(matches(isDisplayed()));

        // Check how many signups are there (should be 1)
        onView(allOf(withId(R.id.mainHeader), isDescendantOfA(withId(R.id.attendee_toolbar))))
                .check(matches(withText("Total Participants: 1")));

        // Match the user name after signup
        onView(withText(userName)).check(matches(isDisplayed()));

        // Get back to event poster page
        pressBack();

        // Get back to ongoing events
        pressBack();

        // Click on signed-Up in ongoing events
        onView(allOf(withId(R.id.signedUpVEventsButton), isDescendantOfA(withId(R.id.eventsTabbar))))
                .perform(click());

        // Check if an event with the specific name exists
        onView(withText("ZZZ+ Annual Conference")).check(matches(isDisplayed()));


    }

    @Test
    public void testParticipants() {

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

        // Click on viewEventCheckin button to view participants
        onView(withId(R.id.viewEventCheckin)).perform(click());

        // Check if the activity is being displayed is correct
        onView(withId(R.id.attendeeListView)).check(matches(isDisplayed()));

        // Check there should be one participant as the event is just created (should be 1)
        onView(allOf(withId(R.id.mainHeader), isDescendantOfA(withId(R.id.attendee_toolbar))))
                .check(matches(withText("Total Participants: 1")));

        // Match the user name after checkIn
        onView(withText(userName)).check(matches(isDisplayed()));
    }

}
