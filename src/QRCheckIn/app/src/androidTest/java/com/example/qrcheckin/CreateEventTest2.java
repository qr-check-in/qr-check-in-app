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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateEventTest2 {
    @Rule
    public ActivityScenarioRule<CreateGenerateEventQR> scenario = new
            ActivityScenarioRule<CreateGenerateEventQR>(CreateGenerateEventQR.class);


    @Test
    public void testMainBarScan() {
        onView(withId(R.id.qrButton)).perform(click());
        try {
            Thread.sleep(5000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.calenderButton)).perform(click());
        try {
            Thread.sleep(5000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.profileButton)).perform(click());
    }

    @Test
    public void testGenerateQR(){
        onView(withId(R.id.makeQRCI)).perform(click());
        onView(withId(R.id.checkInQR)).check(matches(isDisplayed()));
    }


    @Test
    public void testUploadFromGallery(){
        // Check if the button works
        onView(withId(R.id.uploadQRCR)).perform(click());
    }


}
