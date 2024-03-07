package com.example.qrcheckin;

import java.util.ArrayList;

/**
 * Defines a list that holds attendee objects
 */
public class AttendeeList{
    private ArrayList<Attendee> attendees;

    /**
     *
     */
    public AttendeeList() {
        this.attendees = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

    /**
     *
     * @param attendees
     */
    public void setAttendees(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
    }

}
