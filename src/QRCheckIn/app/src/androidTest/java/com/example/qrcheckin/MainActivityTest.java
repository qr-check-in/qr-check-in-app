package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.qrcheckin.Common.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class contains UI tests for the main functionalities of the MainActivity in the QR Check-In application.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Test case to verify the functionality of main navigation bar buttons in MainActivity.
     * Steps:
     * 1. Clicks on the calendar button to view all events.
     * 2. Verifies if the EventListView is displayed.
     * 3. Clicks on the button to create a new event.
     * 4. Verifies if the CreateAddEventDetails screen is displayed.
     * 5. Clicks on the profile button to view user profile details.
     * 6. Verifies if the ProfileActivity is displayed.
     * 7. Clicks on the scan button to access the camera for QR code scanning.
     * 8. Clicks on the button to open the camera.
     */
    @Test
    public void testMainBarScan() {

        // Press allow on notification pop up
        allowNotificationPermission();

        // click on calenderButton to view all events
        onView(withId(R.id.calenderButton)).perform(click());

        // Check if the EventListView is displayed
        onView(withId(R.id.eventListView)).check(matches(isDisplayed()));

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(5000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // click on  to create event
        onView(withId(R.id.addCalenderButton)).perform(click());

        // Check if the CreateAddEventDetails is displayed
        onView(withId(R.id.createAddEventDetails)).check(matches(isDisplayed()));

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(5000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // click on profileButton to view user profile details
        onView(withId(R.id.profileButton)).perform(click());

        // Check if the ProfileActivity is displayed
        onView(withId(R.id.showProfile)).check(matches(isDisplayed()));

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(5000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // click on scanButton to view camera
        onView(withId(R.id.qrButton)).perform(click());

        // click on scanButton to open camera
        onView(withId(R.id.scanButton)).perform(click());

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(5000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to handle notification permission pop-up by clicking on 'Allow'.
     */
    private void allowNotificationPermission() {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject allowButton = uiDevice.findObject(new UiSelector().text("Allow"));
        if (allowButton.exists()) {
            try {
                allowButton.click();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
