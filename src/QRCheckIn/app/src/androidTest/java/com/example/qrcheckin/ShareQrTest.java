package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.qrcheckin.Event.CreateGenerateEventQR;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShareQrTest {

    @Rule
    public ActivityScenarioRule<CreateGenerateEventQR> scenario = new ActivityScenarioRule<CreateGenerateEventQR>(CreateGenerateEventQR.class);

    @Test
    public void testSharingQrWithApps() {

        // perform click on btnGenCheckInQR to generate QR Code
        onView(withId(R.id.btnGenCheckInQR)).perform(click());

        // perform click on firstBox to get full view of QR Code
        onView(withId(R.id.firstBox)).perform(click());

        // perform click on goBack to check if it works
        onView(withId(R.id.goBack)).perform(click());

        // perform click on firstBox to get full view of QR Code
        onView(withId(R.id.firstBox)).perform(click());

        // check if the image view is present in onView
        onView(withId(R.id.qrCodeImageView)).check(matches(isDisplayed()));

        // perform click on shareQR to generate QR Code
        onView(withId(R.id.shareQR)).perform(click());

    }

}
