package com.example.qrcheckin.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Event represents an event created by a user
 */
public class Event implements Serializable {
    private ArrayList<String> attendee;
    private ArrayList<String> signups;
    private ArrayList<String> notifications;
    private ArrayList<String> eventMilestones;
    private HashMap<String, Integer> attendeeMilestone;
    private HashMap<String, Integer> signupMilestone;

    private String organizer;
    private QRCode checkInQRCode;
    private QRCode promoQRCode;
    private EventPoster poster;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventLocation;
    private String eventDescription;
    private int signupLimit;
    private boolean checkInStatus;
    private String topicName;

    /**
     * No argument constructor used by firebase
     */
    public Event() {

    }

    // force pushing 
    /**
     * Constructs a new Event
     *
     * @param organizer        the string FCM t`oken of the organizer's device
     * @param checkInQRCode    the check-in QRCode
     * @param promoQRCode      the promotional PromoQRCode
     * @param poster           the Event's EventPoster
     * @param eventName        the Event's name String
     * @param eventDate        the Event's date String
     * @param eventTime        the Event's time String
     * @param eventLocation    the Event's location String
     * @param eventDescription the Event's description String
     * @param signupLimit the max number of signups for an event
     */
    public Event(String organizer, QRCode checkInQRCode, QRCode promoQRCode, EventPoster poster, String eventName, String eventDate, String eventTime, String eventLocation, String eventDescription, boolean checkInStatus, int signupLimit) {
        this.organizer = organizer;
        this.checkInQRCode = checkInQRCode;
        this.promoQRCode = promoQRCode;
        this.poster = poster;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.checkInStatus = checkInStatus;
        this.signupLimit = signupLimit;
        this.attendee = new ArrayList<String>();
        this.signups = new ArrayList<String>();
        this.notifications = new ArrayList<String>();
        this.eventMilestones = new ArrayList<>();
        this.topicName = topicName;

        this.attendeeMilestone = new HashMap<String, Integer>();
        this.attendeeMilestone.put("1", 0);
        this.attendeeMilestone.put("10", 0);
        this.attendeeMilestone.put("20", 0);
        this.attendeeMilestone.put("30", 0);
        this.attendeeMilestone.put("40", 0);
        this.attendeeMilestone.put("50", 0);
        this.attendeeMilestone.put("75", 0);
        this.attendeeMilestone.put("100", 0);

        this.signupMilestone = new HashMap<String, Integer>();
        this.signupMilestone.put("1", 0);
        this.signupMilestone.put("10", 0);
        this.signupMilestone.put("20", 0);
        this.signupMilestone.put("30", 0);
        this.signupMilestone.put("40", 0);
        this.signupMilestone.put("50", 0);
        this.signupMilestone.put("75", 0);
        this.signupMilestone.put("100", 0);
    }


    public HashMap<String, Integer> getSignupMilestone() {
        return signupMilestone;
    }

    public void setSignupMilestone(HashMap<String, Integer> signupMilestone) {
        this.signupMilestone = signupMilestone;
    }

    public HashMap<String, Integer> getAttendeeMilestone() {
        return attendeeMilestone;
    }

    public void setAttendeeMilestone(HashMap<String, Integer> attendeeMilestone) {
        this.attendeeMilestone = attendeeMilestone;
    }

    public ArrayList<String> getEventMilestones() {
        return eventMilestones;
    }

    public void setEventMilestones(ArrayList<String> eventMilestones) {
        this.eventMilestones = eventMilestones;
    }

    /**
     *
     * @return
     */
    public boolean isCheckInStatus() {
        return checkInStatus;
    }

    /**
     *
     * @param checkInStatus
     */
    public void setCheckInStatus(boolean checkInStatus) {
        this.checkInStatus = checkInStatus;
    }
    /**
     * Returns the maximum number of signups for the event
     * @return
     */
    public int getSignupLimit() {
        return signupLimit;
    }

    /**
     * Sets the maximum number of signups for the event
     * @param signupLimit
     */
    public void setSignupLimit(int signupLimit) {
        this.signupLimit = signupLimit;
    }

    /**
     * Returns the ArrayList of Attendees who have checked into the Event
     * @return attendees ArrayList of Attendees
     */
    public ArrayList<String> getAttendee() {
        return attendee;
    }

    /**
     * Returns the ArrayList of signups who have signed-up for the Event
     * @return signups ArrayList of Attendees
     */
    public ArrayList<String> getSignups() {
        return signups;
    }

    /**
     *
     * @return
     */
    public QRCode getCheckInQRCode() {
        return checkInQRCode;
    }

    /**
     *
     * @param checkInQRCode
     */
    public void setCheckInQRCode(QRCode checkInQRCode) {
        this.checkInQRCode = checkInQRCode;
    }

    /**
     *
     * @return
     */
    public QRCode getPromoQRCode() {
        return promoQRCode;
    }

    /**
     *
     * @param promoQRCode
     */
    public void setPromoQRCode(QRCode promoQRCode) {
        this.promoQRCode = promoQRCode;
    }

    /**
     *
     * @return
     */
    public EventPoster getPoster() {
        return poster;
    }

    /**
     *
     * @param poster
     */
    public void setPoster(EventPoster poster) {
        this.poster = poster;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getNotifications() {
        return notifications;
    }

    /**
     *
     * @param notifications
     */
    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    /**
     *
     * @return
     */
    public String getEventName() {
        return eventName;
    }

    /**
     *
     * @param eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * gets the Event's date
     * @return string in the format "yyyy-mm-dd"
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * sets the Event's date
     * @param eventDate String in the format "yyyy-mm-dd"
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     *
     * @return
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     *
     * @param eventTime
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    /**
     *
     * @return
     */
    public String getEventLocation() {
        return eventLocation;
    }

    /**
     *
     * @param eventLocation
     */
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    /**
     *
     * @return
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     *
     * @param eventDescription
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     *
     * @return
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     *
     * @param organizer
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
