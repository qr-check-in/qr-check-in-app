package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.qrcheckin.Admin.AdminViewProfiles;
import com.example.qrcheckin.Admin.ProfileActivityAdmin;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for viewing and deleting profiles in the Admin interface.
 */
@RunWith(AndroidJUnit4.class)
public class AdminDisplayProfilesTest {

    @Rule
    public IntentsTestRule<AdminViewProfiles> intentsTestRule = new IntentsTestRule<>(AdminViewProfiles.class);

    @Test
    public void viewAndDeleteProfile() {
        try {
            Thread.sleep(5000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.profiles)).check(matches(isDisplayed()));
        try {
            Thread.sleep(2000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final int[] initialItemCount = new int[1];
        onView(withId(R.id.profile_recycler_view)).check((view, noViewFoundException) -> {
            RecyclerView recyclerView = (RecyclerView) view;
            initialItemCount[0] = recyclerView.getAdapter().getItemCount();
        });try {
            Thread.sleep(2000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Assuming the RecyclerView is populated
        // Click on the first profile in the list to view its details
        onView(withId(R.id.profile_recycler_view)).perform(actionOnItemAtPosition(0, click()));

        // Verify that the detail activity is launched with the correct intent
        intended(hasComponent(ProfileActivityAdmin.class.getName()));

        // Verify the profile detail is displayed by checking for a unique element (e.g., name or id)
        onView(withId(R.id.profileToolbar)).check(matches(isDisplayed()));
        try {
            Thread.sleep(5000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Perform the delete action
        onView(withId(R.id.btnRemoveProfile)).perform(click());
        try {
            Thread.sleep(5000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Verify the profile has been deleted
        // This might involve checking for a confirmation message or verifying the list has been updated
        onView(withId(R.id.profile_recycler_view)).check(matches(isDisplayed()));
    }
}
