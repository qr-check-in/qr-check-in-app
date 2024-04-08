package com.example.qrcheckin;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static org.hamcrest.Matchers.allOf;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.Intents.intended;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;

import com.example.qrcheckin.Event.EventListView;
import com.example.qrcheckin.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventListTest {
    @Rule
    public ActivityScenarioRule<EventListView> scenario = new
            ActivityScenarioRule<EventListView>(EventListView.class);

    /**
     * Tests the main bar scan
     */
    @Test
    public void testMainBarScan(){
        onView(withId(R.id.qrButton)).perform(click());
        onView(withId(R.id.welcomeToAppTest)).check(matches(withText("Welcome to QRCheckIn")));
    }

    /**
     * tests main bra add event screen
     */
    @Test
    public void testMainBarEvent(){
        onView(withId(R.id.addCalenderButton)).perform(click());
        onView(withId(R.id.eventNameLayout)).check(matches(isDisplayed()));
    }

    /**
     * tests main bar profile screen
     */
    @Test
    public void testMainBarProfile(){
        onView(withId(R.id.profileButton)).perform(click());
//        onView(withId(R.id.profile1)).check(matches(isDisplayed()));
    }

    /**
     * tests clicking item from recycler view
     */
    @Test
    public void testListItem(){
        // openai, 2024, chatgpt, how can I make it select the first item? in a ui test
        // wait a little before clicking
        try {
            Thread.sleep(2000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform a click action on the first item in the list
        onData(anything())
                .inAdapterView(withId(R.id.event_recycler_view)) // Replace "your_list_view_id" with the ID of your ListView
                .atPosition(0)
                .perform(click());
    }

}
