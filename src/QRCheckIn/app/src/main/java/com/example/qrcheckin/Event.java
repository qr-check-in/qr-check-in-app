package com.example.qrcheckin;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Event represents an event created by a user
 */
public class Event implements Serializable {
    private ArrayList<String> attendee;
    private ArrayList<String> signups;
    private String organizer;
    private QRCode checkInQRCode;
    private PromoQRCode promoQRCode;
    private EventPoster poster;
    private ArrayList<Notification> notifications;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventLocation;
    private String eventDescription;

    private int numberofAttendees;
    private boolean checkInStatus;


    /**
     * No argument constructor used by firebase
     */
    public Event() {

    }

    // force pushing 
    /**
     * Constructs a new Event
     *
     * @param organizer        the string FCM token of the organizer's device
     * @param checkInQRCode    the check-in QRCode
     * @param promoQRCode      the promotional PromoQRCode
     * @param poster           the Event's EventPoster
     * @param eventName        the Event's name String
     * @param eventDate        the Event's date String
     * @param eventTime        the Event's time String
     * @param eventLocation    the Event's location String
     * @param eventDescription the Event's description String
     * @param numberofAttendees the max number of attendees for an event
     */
    public Event(String organizer, QRCode checkInQRCode, PromoQRCode promoQRCode, EventPoster poster, String eventName, String eventDate, String eventTime, String eventLocation, String eventDescription, boolean checkInStatus, int numberofAttendees) {
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
        this.numberofAttendees = numberofAttendees;
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
     * Returns the maxiumum number of attendees that can attend an event
     * @return
     */
    public int getNumberofAttendees() {
        return numberofAttendees;
    }

    /**
     * Sets the total number attendees an event can hold
     * @param numberofAttendees
     */
    public void setNumberofAttendees(int numberofAttendees) {
        this.numberofAttendees = numberofAttendees;
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
    public PromoQRCode getPromoQRCode() {
        return promoQRCode;
    }

    /**
     *
     * @param promoQRCode
     */
    public void setPromoQRCode(PromoQRCode promoQRCode) {
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
    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    /**
     *
     * @param notifications
     */
    public void setNotifications(ArrayList<Notification> notifications) {
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
}
