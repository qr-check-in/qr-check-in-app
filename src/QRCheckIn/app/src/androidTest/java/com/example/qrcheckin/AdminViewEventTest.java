//package com.example.qrcheckin;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//import androidx.test.espresso.intent.Intents;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.scrollTo;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.not;
//
//import com.example.qrcheckin.Admin.AdminEventPage;
//import com.example.qrcheckin.Admin.AdminViewEvent;
//import com.example.qrcheckin.Admin.ProfileActivityAdmin;
//
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class AdminViewEventTest {
//
//    @Rule
//    public ActivityScenarioRule<AdminViewEvent> activityRule = new ActivityScenarioRule<>(AdminViewEvent.class);
//
//    @Before
//    public void setUp() {
//        Intents.init();
//    }
//
//    @After
//    public void tearDown() {
//        Intents.release();
//    }
//
//    @Test
//    public void viewAndDeleteEvent() {
//        // Obtain the initial item count from the RecyclerView
//        final int[] initialItemCount = new int[1];
//        onView(withId(R.id.event_recycler_view)).check((view, noViewFoundException) -> {
//            RecyclerView recyclerView = (RecyclerView) view;
//            initialItemCount[0] = recyclerView.getAdapter().getItemCount();
//        });
//
//        // Click on the first event
////        onView(withId(R.id.event_recycler_view))
////                .perform(actionOnItemAtPosition(0, click()));
//
//        intended(hasComponent(ProfileActivityAdmin.class.getName()));
//        intended(hasExtraWithKey("DOCUMENT_ID"));
//        onView(withId(R.id.event_recycler_view))
//                .perform(androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition(0, click()));
//        onView(withId(R.id.delete_button))
//                .perform(scrollTo()) // Scroll to the delete button if not in view
//                .perform(click());
//        onView(withId(R.id.textview_name))
//                .check(matches(not(withText("Name of the Deleted Profile"))));
//        // Now we assume that we are back in the list of events
//        // If you're not automatically returned to the event list after deletion, you may need to add a step to navigate back
//
//        // Check the number of items in the RecyclerView has decreased by 1 after deletion
//        onView(withId(R.id.event_recycler_view))
//                .check(new RecyclerViewItemCountAssertion(initialItemCount[0] - 1));
//    }
//
//}
//
