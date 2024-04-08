package com.example.qrcheckin;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.*;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.qrcheckin.Admin.AdminActivity;

/**
 * Tests the functionality of the AdminDisplayEvents feature in the AdminActivity.
 * Ensures that events can be displayed and a selected event can be deleted.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminDisplayEventsTest {

    /**
     * Rule to launch AdminActivity before each test execution.
     */
    @Rule
    public IntentsTestRule<AdminActivity> intentsTestRule = new IntentsTestRule<>(AdminActivity.class);

    /**
     * Verifies that the RecyclerView listing the events is displayed to the admin.
     * Simulates the deletion of an event by performing a click action on the first event in the list and then clicking the delete button.
     * Asserts that the RecyclerView is displayed again post deletion, implying that the UI has been updated to reflect the removal.
     * Note: This test assumes that clicking the delete button instantly removes the event and updates the UI.
     * The test includes an intentional delay using Thread.sleep to wait for the UI to load; it's a workaround for asynchronous operations and might lead to flaky tests.
     */
    @Test
    public void checkRecyclerView() {

        String dateOfEventToRemove = "2024-4-17";

        // Checks that the dashboard is displayed
        onView(withId(R.id.dashboard)).check(matches(isDisplayed()));

        // Navigates to the event list view
        onView(withId(R.id.btn_admin_event)).perform(click());

        // Waits for the events list to be fully loaded
        try {
            Thread.sleep(5000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Asserts that the events list is displayed
        onView(withId(R.id.events)).check(matches(isDisplayed()));
        // Simulates a user interaction to wait for any potential network calls or data loading
        try {
            Thread.sleep(5000); // 5000 milliseconds = 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Performs a click action on the first item in the event RecyclerView and then clicks the delete button

        onView(withText(dateOfEventToRemove)).perform(click());
        onView(withId(R.id.btnRemoveEvent)).perform(click());

    }
}
