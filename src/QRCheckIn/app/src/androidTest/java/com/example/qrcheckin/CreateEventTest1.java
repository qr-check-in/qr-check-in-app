package com.example.qrcheckin;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.*;
import androidx.test.filters.LargeTest;
//import androidx.test.espresso.contrib.PickerActions;
import android.content.Intent;
import android.widget.DatePicker;

import static org.hamcrest.Matchers.allOf;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
public class CreateEventTest1 {
    @Rule
    public ActivityScenarioRule<CreateNewEventScreen1> scenario = new
            ActivityScenarioRule<CreateNewEventScreen1>(CreateNewEventScreen1.class);

//    @Test
//    public void testDatePicker() {
//        // Initialize Espresso Intents
//        Intents.init();
//
//        // Perform click action on the "Select Date" button
//        onView(withId(R.id.eventDatePicker)).perform(click());
//
//        // Check if the DatePicker dialog is displayed
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(matches(isDisplayed()));
//
//        // Set a specific date on the DatePicker dialog
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 10, 15));
//
//        // Confirm the selected date
//        onView(withText("OK")).perform(click());
//
//        // Check if the selected date is displayed in the date TextView
//        onView(withId(R.id.eventDateText)).check(matches(withText("2023-11-15")));
//
//        // Release Espresso Intents
//        Intents.release();
//    }

    @Test
    public void testButtonsAndTextboxes(){
        onView(withId(R.id.eventNameText)).perform(ViewActions.typeText("Test Event1"));
        onView(withId(R.id.eventNameText)).perform(ViewActions.typeText("Test Event1"));
        onView(withId(R.id.uploadPosterButton)).perform(click());

    }
}
