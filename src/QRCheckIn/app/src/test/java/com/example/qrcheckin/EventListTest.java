package com.example.qrcheckin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventListTest {

    private EventList mockEventList(){
        EventList eventList = new EventList();
        eventList.add(mockEvent());
        return eventList;
    }

    private Event mockEvent(){
        return new Event(null, null, null, null,
                "testEvent1", "12:00", "University of Alberta",
                "Unit test for event", false);
    }
    @Test
    void testAdd(){
        EventList eventList = mockEventList();

        assertEquals(1, eventList.getEvents().size());

        Event event = new Event(null, null, null, null,
                "testEvent2", "1:00", "University of Alberta",
                "Unit test for event2", true);

        asserEquals(2, eventList.getEvents().size());
        assertTrue(eventList.getEvents().contains(event));
    }

    @Test
    void testAddException(){
        EventList eventList = mockEventList();

        Event event = new Event(null, null, null, null,
                "testEvent3", "2:00", "University of Alberta",
                "Unit test for event3", false);

        eventList.add(event);

        assertThrows(IllegalArgumentException.class,()->{
            eventList.add(event);
        });
    }
}
