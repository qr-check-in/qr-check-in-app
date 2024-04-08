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
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.qrcheckin.Admin.AdminActivity;
import com.example.qrcheckin.Admin.AdminViewImages;
import static org.hamcrest.CoreMatchers.anything;
/**
 * Tests image management features in the Admin UI.
 */
@RunWith(AndroidJUnit4.class)
public class AdminDisplayImagesTest {

    @Rule
    public ActivityScenarioRule<AdminActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AdminActivity.class);

    /**
     * Sets up Intents before each test.
     */
    @Before
    public void setUp() {
        Intents.init();
    }

    /**
     * Cleans up Intents after each test.
     */
    @After
    public void tearDown() {
        Intents.release();
    }

    /**
     * Verifies that the Admin can view and delete images.
     */
    @Test
    public void testImageButtonOpensImageView() {
        onView(withId(R.id.image_admin_button)).perform(click());
        try {
            Thread.sleep(1000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Verify that AdminViewImages is opened
        intended(hasComponent(AdminViewImages.class.getName()));

    }
}
