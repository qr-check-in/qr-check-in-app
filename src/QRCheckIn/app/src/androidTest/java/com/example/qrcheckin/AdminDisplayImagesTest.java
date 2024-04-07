package com.example.qrcheckin;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;

@RunWith(AndroidJUnit4.class)
public class AdminDisplayImagesTest {
    @Rule
    public ActivityScenarioRule<AdminActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AdminActivity.class);

    @Before
    public void setUp() {
        // Initialize Espresso Intents
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release Espresso Intents after test
        Intents.release();
    }

    @Test
    public void testImageButtonOpensImageView() {
        // Perform a click on the image list button
        onView(withId(R.id.image_admin_button)).perform(click());

        // Verify that AdminViewImages is opened
        intended(hasComponent(AdminViewImages.class.getName()));
    }
}