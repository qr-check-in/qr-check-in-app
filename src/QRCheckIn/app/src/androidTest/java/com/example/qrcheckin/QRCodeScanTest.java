package com.example.qrcheckin;


import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.qrcheckin.Common.MainActivity;
import com.example.qrcheckin.Common.QRCodeScan;

@RunWith(AndroidJUnit4.class)
public class QRCodeScanTest {

    @Rule
    public ActivityTestRule<QRCodeScan> activityTestRule = new ActivityTestRule<>(QRCodeScan.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void testActivityLaunches() {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.topNavigationText)).check(matches(isDisplayed()));
        onView(withId(R.id.scanButton)).perform(click());

    }


    @After
    public void tearDown() {
        Intents.release();
    }
}