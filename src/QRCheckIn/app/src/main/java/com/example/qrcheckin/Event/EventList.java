package com.example.qrcheckin.Event;

import java.util.ArrayList;

/**
 * Represents collection Events.
 * manage a list of events; adding, retrieving, setting.
 */
public class EventList {
    /**
     * Stores the list of events.
     */
    private ArrayList<Event> events;

    /**
     * Constructs an empty list of events.
     */
    public EventList() {
        this.events = new ArrayList<>();
    }

    /**
     * Returns the list of events.
     *
     * @return ArrayList of Event objects.
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Updates the list of events.
     *
     * @param events An ArrayList of Event objects to be set as the current list of events.
     */
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
