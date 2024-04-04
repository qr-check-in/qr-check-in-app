package com.example.qrcheckin;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminViewEventTest {

    @Rule
    public ActivityScenarioRule<AdminViewEvent> activityRule = new ActivityScenarioRule<>(AdminViewEvent.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void selectEventOpensDetailPage() {
        // Assuming you have a RecyclerView with ID event_recycler_view and want to click on the first item
        onView(withId(R.id.event_recycler_view)).perform(actionOnItemAtPosition(0, click()));

        // Then verify the expected behavior, for example, that AdminEventPage is opened
        intended(hasComponent(AdminEventPage.class.getName()));
    }
}

