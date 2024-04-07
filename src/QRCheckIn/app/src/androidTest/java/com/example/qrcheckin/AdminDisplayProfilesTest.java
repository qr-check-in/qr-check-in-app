//package com.example.qrcheckin;
//
//import androidx.test.espresso.Espresso;
//import androidx.test.espresso.intent.Intents;
//import androidx.test.espresso.intent.rule.IntentsTestRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//import com.example.qrcheckin.Admin.AdminActivity;
//import com.example.qrcheckin.Admin.AdminViewProfiles;
//
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class AdminDisplayProfilesTest {
//
//    @Rule
//    public IntentsTestRule<AdminActivity> intentsTestRule = new IntentsTestRule<>(AdminActivity.class);
//
//    @Test
//    public void profileButtonOpensProfileView() {
//        // Perform a click on the profiles button
//        onView(withId(R.id.profile_admin_button)).perform(click());
//
//        // Wait for 5 seconds
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Continue with your test
//        intended(hasComponent(AdminViewProfiles.class.getName()));
//    }
//
//}
