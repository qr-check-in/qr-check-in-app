package com.example.qrcheckin;

import java.util.ArrayList;

public class EventList {
    private ArrayList<Event> events;

    public EventList() {
        this.events = new ArrayList<>();
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
