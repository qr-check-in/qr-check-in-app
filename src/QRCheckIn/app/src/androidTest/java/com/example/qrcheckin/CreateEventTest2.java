package com.example.qrcheckin;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.*;
import androidx.test.filters.LargeTest;
import android.content.Intent;
import android.widget.DatePicker;

import static org.hamcrest.Matchers.allOf;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;


import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.intent.Intents;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;
import static androidx.test.espresso.intent.Intents.intended;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateEventTest2 {
    @Rule
    public ActivityScenarioRule<CreateNewEventScreen2> scenario = new
            ActivityScenarioRule<CreateNewEventScreen2>(CreateNewEventScreen2.class);


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
    public void testUploadFromGallery(){
        // Check if the button works
        onView(withId(R.id.uploadQRCR)).perform(click());
    }
}
