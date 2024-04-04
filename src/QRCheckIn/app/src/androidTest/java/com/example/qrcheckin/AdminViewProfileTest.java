package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AdminViewProfileTest {

    @Rule
    public ActivityScenarioRule<AdminViewProfiles> activityScenarioRule = new ActivityScenarioRule<>(AdminViewProfiles.class);
    public IntentsTestRule<AdminViewProfiles> intentsTestRule = new IntentsTestRule<>(AdminViewProfiles.class);

    @Before
    public void setup() {
        Intents.init();
        // Additional setup for mocking data or initializing test environment
    }

    @After
    public void tearDown() {
        Intents.release();
        // Clean up and release resources
    }

    @Test
    public void selectProfileOpensDetail() {
        // Assuming the RecyclerView is populated
        onView(withId(R.id.profile_recycler_view)).perform(actionOnItemAtPosition(0, click()));

        // Verify that the detail activity is launched with the correct intent
        intended(hasComponent(ProfileActivityAdmin.class.getName()));
        // Verify the intent contains the expected extras, such as the DOCUMENT_ID
        intended(hasExtraWithKey("DOCUMENT_ID"));
    }
}
