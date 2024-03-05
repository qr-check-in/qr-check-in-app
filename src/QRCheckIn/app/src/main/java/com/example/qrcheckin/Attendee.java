package com.example.qrcheckin;

import android.location.Location;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * A class that defines an app user as an Attendee
 */
public class Attendee {
    private ArrayList<Event> attendedEvents;
    private ArrayList<Event> signupEvents;
    private Profile profile;
    private Location location;

    public Attendee() throws FileNotFoundException {
        this.attendedEvents = new ArrayList<Event>();
        this.signupEvents = new ArrayList<Event>();
        this.profile = new Profile(this);
    }

    /**
     * Returns a list of Events the Attendee has checked-in to
     * 
     * @return EventList of attended Events
     */
    public ArrayList<Event> getAttendedEvents() {
        return attendedEvents;
    }

    /**
     * Returns a list of Events the Attendee has signed up for
     * 
     * @return EventList of signed up Events
     */
    public ArrayList<Event> getSignupEvents() {
        return signupEvents;
    }

    /**
     * Returns the Profile of the Attendee
     * 
     * @return Profile of the Attendee
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the Profile of the Attendee
     * 
     * @param profile Profile of the Attendee
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Returns the Location of the Attendee
     * 
     * @return Location of the Attendee
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the Location of the Attendee
     * 
     * @param location Location of the Attendee
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}
