package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.qrcheckin.Event.EventListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpTest {
    String eventName = "ZZZ+ Annual Conference";

    @Rule
    public ActivityScenarioRule<EventListView> scenario = new ActivityScenarioRule<EventListView>(EventListView.class);

    @Before
    public void setUp() {
        // Initialize Intents
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release Intents
        Intents.release();
    }

    @Test
    public void testEventCheckIn(){

        // Check if the activity is being displayed
        onView(withId(R.id.eventListView)).check(matches(isDisplayed()));

        // Sleep for 1 second load the activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on the event (ZZZ+ Annual Conference) in the list
        onView(withText(eventName)).perform(click());

        // Click on the signup button CheckBox
        onView(withId(R.id.signup_button)).perform(click());

        // Press the back button
        onView(isRoot()).perform(pressBack());

        // Check if the activity is being displayed
        onView(withId(R.id.eventListView)).check(matches(isDisplayed()));

        // press myEventsButton to see events created by user
        onView(ViewMatchers.withId(R.id.eventsTabbar)).check(matches(isDisplayed())).perform(click());

        // press signedUpVEventsButton to get events you are signed up for
        onView(ViewMatchers.withId(R.id.signedUpVEventsButton))
                .check(matches(isDisplayed())).perform(click());

        // Click on the event with text stored in title
        onView(withText(eventName)).perform(click());

        // Check if the activity is being displayed
        onView(withId(R.id.eventPosterDetailsPresent)).check(matches(isDisplayed()));
    }

}
