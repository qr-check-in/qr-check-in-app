package com.example.qrcheckin;

import java.util.ArrayList;

public class EventList {
    private ArrayList<Event> Events;

    @Override
    public void onCreate(){
        super.onCreate();
        Events = new ArrayList<Event>()
    }
    public ArrayList<Event> getEvents() {
        return Events;
    }

    public void setEvents(ArrayList<Event> events) {
        Events = events;
    }
}
