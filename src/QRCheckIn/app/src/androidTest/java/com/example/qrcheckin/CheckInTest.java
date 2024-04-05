package com.example.qrcheckin;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.qrcheckin.Event.CreateAddEventDetails;

import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CheckInTest {

    @Rule
    public ActivityScenarioRule<CreateAddEventDetails> scenario = new
            ActivityScenarioRule<CreateAddEventDetails>(CreateAddEventDetails.class);
}
