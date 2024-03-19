package com.example.qrcheckin;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class AttendeeTest {
    private Attendee attendee;

    @Before
    public void setUp() {
        attendee = new Attendee();
    }

    @Test
    public void testInitialAttendeeState() {
        assertNotNull("Attendee's attended events list should not be null", attendee.getAttendedEvents());
        assertTrue("Attendee's attended events list should be empty initially", attendee.getAttendedEvents().isEmpty());

        assertNotNull("Attendee's signup events list should not be null", attendee.getSignupEvents());
        assertTrue("Attendee's signup events list should be empty initially", attendee.getSignupEvents().isEmpty());

        assertNotNull("Attendee's profile should not be null", attendee.getProfile());
    }

}
