package com.example.qrcheckin;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.qrcheckin.Admin.AdminActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class tests the visibility and functionality of buttons on the Admin Dashboard within the AdminActivity.
 * It verifies the presence of essential buttons for navigating to different sections of the admin interface
 * and ensures that tapping these buttons takes the user to the expected screen.
 */
@RunWith(AndroidJUnit4.class)
public class AdminDashboardTest {

    /**
     * Launches the AdminActivity before each test execution.
     */
    @Rule
    public ActivityScenarioRule<AdminActivity> activityRule =
            new ActivityScenarioRule<>(AdminActivity.class);

    /**
     * Tests that the buttons for navigating to events, profiles, and images management sections exist.
     * Verifies that each button is displayed on the Admin dashboard.
     */
    @Test
    public void testButtonsExist() {
        // Checks for the visibility of the button to view events
        onView(ViewMatchers.withId(R.id.btn_admin_event))
                .check(matches(isDisplayed()));
        // Checks for the visibility of the button to view profiles
        onView(withId(R.id.profile_admin_button))
                .check(matches(isDisplayed()));
        // Checks for the visibility of the button to view images
        onView(withId(R.id.image_admin_button))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests the functionality of the "View Events" button.
     * Ensures that clicking on the "View Events" button navigates to the events management section.
     */
    @Test
    public void testViewEvents() {
        // Clicks on the button to view events
        onView(withId(R.id.btn_admin_event)).perform(click());
        // Checks that the events management section is displayed
        onView(withId(R.id.events)).check(matches(isDisplayed()));
    }

    /**
     * Tests the functionality of the "View Profiles" button.
     * Ensures that clicking on the "View Profiles" button navigates to the profiles management section.
     */
    @Test
    public void testViewProfiles() {
        // Clicks on the button to view profiles
        onView(withId(R.id.profile_admin_button)).perform(click());
        // Checks that the profiles management section is displayed
        onView(withId(R.id.profiles)).check(matches(isDisplayed()));
    }

    /**
     * Tests the functionality of the "View Images" button.
     * Ensures that clicking on the "View Images" button navigates to the images management section.
     */
    @Test
    public void testViewImages() {
        // Clicks on the button to view images
        onView(withId(R.id.image_admin_button)).perform(click());
        // Checks that the images management section is displayed
        onView(withId(R.id.images)).check(matches(isDisplayed()));
    }
}
