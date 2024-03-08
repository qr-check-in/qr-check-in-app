package com.example.qrcheckin;

import java.util.ArrayList;

/**
 * Represents list of attendees for an event.
 * Manages collection of Attendee objects,
 * has methods to manipulate and access the list.
 */
public class AttendeeList {
    /**
     * Stores a list of Attendee objects.
     */
    private ArrayList<Attendee> attendees;

    /**
     * Constructs an empty list of attendees.
     */
    public AttendeeList() {
        this.attendees = new ArrayList<>();
    }

    /**
     * Returns the list of attendees.
     *
     * @return A list containing Attendee objects.
     */
    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

    /**
     * Updates the list of attendees.
     *
     * @param attendees A list of Attendee objects to replace the current list.
     */
    public void setAttendees(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
    }
}
