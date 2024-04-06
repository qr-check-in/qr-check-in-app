package com.example.qrcheckin.Attendee;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that defines an app user as an Attendee
 */
public class Attendee {
    private HashMap<String, Integer> attendedEvents;
    private ArrayList<String> signupEvents;
    private Profile profile;
    private GeoPoint location;


    /**
     * Constructs a new Attendee
     */
    public Attendee(){
        this.attendedEvents = new HashMap<>();
        this.signupEvents = new ArrayList<String>();
        this.profile = new Profile();
    }

    /**
     * Returns a list of Events the Attendee has checked-in to
     *
     * @return EventList of attended Events
     */
    public HashMap<String, Integer> getAttendedEvents() {
        return attendedEvents;
    }

    /**
     * Returns a list of Events the Attendee has signed up for
     * @return EventList of signed up Events
     */
    public ArrayList<String> getSignupEvents() {
        return signupEvents;
    }

    /**
     * Returns the Profile of the Attendee
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
     * @return Location of the Attendee
     */
    public GeoPoint getLocation() {
        return location;
    }

    /**
     * Sets the Location of the Attendee
     * @param location Location of the Attendee
     */
    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}