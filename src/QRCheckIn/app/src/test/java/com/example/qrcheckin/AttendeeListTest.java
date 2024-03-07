package com.example.qrcheckin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AttendeeListTest {

    private AttendeeList mockAttendeeList(){
        AttendeeList attendeeList = new AttendeeList();
        attendeeList.add(mockAttendee());
        return attendeeList;
    }

    private Attendee mockAttendee(){
        return new Attendee(null, null, null, null);
    }
    @Test
    void testAdd(){
        AttendeeList attendeeList = AttendeeList();

        assertEquals(1, attendeeList.getEvents().size());

        Attendee attendee = new Attendee(null, null, null, null);

        asserEquals(2, attendeeList.getAttendees().size());
        assertTrue(attendeeList.getAttendees().contains(attendee));
    }

    @Test
    void testAddException(){
        AttendeeList attendeeList = mockAttendeeList();

        Attendee attendee = new Attendee(null, null, null, null);

        attendeeList.add(attendee);

        assertThrows(IllegalArgumentException.class,()->{
            attendeeList.add(attendee);
        });
    }
}
