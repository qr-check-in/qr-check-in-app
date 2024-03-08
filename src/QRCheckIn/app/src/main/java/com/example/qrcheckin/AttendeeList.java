package com.example.qrcheckin;

import java.util.ArrayList;

public class AttendeeList{
    private ArrayList<Attendee> attendees;

    public AttendeeList() {
        this.attendees = new ArrayList<>();
    }
    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
    }

}
