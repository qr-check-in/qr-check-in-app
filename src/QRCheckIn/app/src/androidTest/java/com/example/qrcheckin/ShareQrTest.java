package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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


/**
 * This class contains UI tests for sharing QR codes in the QR Check-In application.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShareQrTest {

    @Rule
    public ActivityScenarioRule<CreateAddEventDetails> scenario = new ActivityScenarioRule<CreateAddEventDetails>(CreateAddEventDetails.class);

    private EventUITest createEventAndCheck;

    @Before
    public void setUp() {
        // Initialize Intents
        Intents.init();

        // setup for testing notifications
        // creates a event to push notifications
        createEventAndCheck = new EventUITest();
        createEventAndCheck.testMatchPosterDetails();
    }

    @After
    public void tearDown() {
        // Release Intents
        Intents.release();
    }

    /**
     * Test case to verify sharing QR code with other apps.
     * Steps:
     * 1. Checks if the activity (event poster) is displayed.
     * 2. Opens the bottom sheet to access QR code.
     * 3. Clicks to view the QR code.
     * 4. Checks if the QR code image view is displayed.
     * 5. Clicks to share the QR code.
     */
    @Test
    public void testSharingQrWithApps() {

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

        // perform click on viewCheckInQRCode to get QR code view
        onView(withId(R.id.viewCheckInQRCode)).perform(click());

        // Check if the activity is being displayed is correct
        onView(withId(R.id.qrCodeImageView)).check(matches(isDisplayed()));

        // perform click on shareQR to generate QR Code
        onView(withId(R.id.shareQR)).perform(click());

    }

}
