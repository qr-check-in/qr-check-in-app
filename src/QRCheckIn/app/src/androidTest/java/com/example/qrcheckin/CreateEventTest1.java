package com.example.qrcheckin;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static org.hamcrest.Matchers.allOf;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.Intents.intended;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateEventTest1 {
    @Rule
    public ActivityScenarioRule<CreateAddEventDetails> scenario = new
            ActivityScenarioRule<CreateAddEventDetails>(CreateAddEventDetails.class);

    /**
     * tests main bar scan
     */
    @Test
    public void testMainBarScan(){
        onView(withId(R.id.qrButton)).perform(click());
        onView(withId(R.id.welcomeToAppTest)).check(matches(withText("Welcome to QRCheckIn")));
    }

    /**
     * tests main bar event navigation
     */
    @Test
    public void testMainBarEvents(){
        onView(withId(R.id.calenderButton)).perform(click());
        onView(withId(R.id.eventListToolbar)).check(matches(isDisplayed()));
    }

    /**
     * tests main bar profile button navigation
     */
    @Test
    public void testMainBarProfile(){
        onView(withId(R.id.profileButton)).perform(click());
//        onView(withId(R.id.profile1)).check(matches(isDisplayed()));
    }

    /**
     * tests if the button to open the gallery works
     */
    @Test
    public void testOpenGallery(){
        // Check if the button works
        onView(withId(R.id.uploadPosterButton)).perform(click());
    }

    /**
     * Tests if navigation to createNewEventScreen2
     */
    @Test
    public void testNextButton(){
        onView(withId(R.id.nextButton)).perform(click());
        onView(withId(R.id.firstBox)).check(matches(isDisplayed()));
    }

    /**
     * Tests the text boxes and buttons on the page
     */
    @Test
    public void testButtonsAndTextboxes(){
        // Test the textboxes
        onView(withId(R.id.eventNameText)).perform(ViewActions.typeText("Test Event1"));
        onView(withId(R.id.eventDateText)).perform(ViewActions.typeText("2024-3-20"));
        onView(withId(R.id.eventTimeText)).perform(ViewActions.typeText("15:30"));
        onView(withId(R.id.eventLocationText)).perform(ViewActions.typeText("University of Alberta"));

        // Close the keybboard
        onView(withId(R.id.eventDescriptionText)).perform(ViewActions.closeSoftKeyboard());

        // Test event description
        onView(withId(R.id.eventDescriptionText)).perform(ViewActions.typeText("Testing description"));

        // Test the switch
        onView(withId(R.id.checkInSwitch)).perform(ViewActions.click());

    }
}
