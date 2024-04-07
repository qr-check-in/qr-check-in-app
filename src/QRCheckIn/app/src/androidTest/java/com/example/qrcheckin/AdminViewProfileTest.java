//package com.example.qrcheckin;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
////import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.test.espresso.NoMatchingViewException;
//import androidx.test.espresso.ViewAssertion;
//import androidx.test.espresso.intent.Intents;
//import androidx.test.espresso.intent.rule.IntentsTestRule;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.scrollTo;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.not;
//
//import android.view.View;
//
//import com.example.qrcheckin.Admin.AdminViewProfiles;
//import com.example.qrcheckin.Admin.ProfileActivityAdmin;
//import androidx.test.espresso.contrib.RecyclerViewActions.PositionableRecyclerViewAction;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//
//@RunWith(AndroidJUnit4.class)
//public class AdminViewProfileTest {
//
//    @Rule
//    public ActivityScenarioRule<AdminViewProfiles> activityScenarioRule = new ActivityScenarioRule<>(AdminViewProfiles.class);
//    public IntentsTestRule<AdminViewProfiles> intentsTestRule = new IntentsTestRule<>(AdminViewProfiles.class);
//
//    @Before
//    public void setup() {
//        Intents.init();
//        // Additional setup for mocking data or initializing test environment
//    }
//
//    @After
//    public void tearDown() {
//        Intents.release();
//        // Clean up and release resources
//    }
//
//    @Test
//    public void selectProfileOpensDetail() {
//              // Assuming the RecyclerView is populated
//        //onView(withId(R.id.profile_recycler_view)).perform(actionOnItemAtPosition(0, click()));
//        final int[] initialItemCount = new int[1];
//        onView(withId(R.id.profile_recycler_view)).check((view, noViewFoundException) -> {
//            RecyclerView recyclerView = (RecyclerView) view;
//            initialItemCount[0] = recyclerView.getAdapter().getItemCount();
//        });
//        int expectedItemCountAfterDeletion = initialItemCount[0] - 1;
//        // Verify that the detail activity is launched with the correct intent
//        intended(hasComponent(ProfileActivityAdmin.class.getName()));
//        intended(hasExtraWithKey("DOCUMENT_ID"));
//        onView(withId(R.id.profile_recycler_view))
//                .perform(androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition(0, click()));
//        onView(withId(R.id.delete_button))
//                .perform(scrollTo()) // Scroll to the delete button if not in view
//                .perform(click());
//        onView(withId(R.id.textview_name))
//                .check(matches(not(withText("Name of the Deleted Profile"))));
//        onView(withId(R.id.profile_recycler_view))
//                .check(new RecyclerViewItemCountAssertion(expectedItemCountAfterDeletion));
//
//
//        // Verify the intent contains the expected extras, such as the DOCUMENT_ID
//
//
//    }
//}
