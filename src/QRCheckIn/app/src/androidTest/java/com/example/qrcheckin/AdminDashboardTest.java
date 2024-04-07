package com.example.qrcheckin;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AdminDashboardTest {

    @Rule
    public ActivityScenarioRule<AdminActivity> activityRule =
            new ActivityScenarioRule<>(AdminActivity.class);

    @Test
    public void testButtonsExist() {


        Espresso.onView(ViewMatchers.withId(R.id.btn_admin_event))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.profile_admin_button))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.image_admin_button))
                .check(matches(isDisplayed()));
    }
}
