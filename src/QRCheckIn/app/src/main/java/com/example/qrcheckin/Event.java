package com.example.qrcheckin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Event implements Serializable {
    private ArrayList<Attendee> attendees;
    private ArrayList<Attendee> signups;
    private final UUID eventId;
    private QRCode checkInQRCode;
    private PromoQRCode promoQRCode;
    private EventPoster poster;
    private ArrayList<Notification> notifications;
    private String eventName;
    private String eventTime;
    private String eventLocation;
    private String eventDescription;

    /**
     * Constructs a new Event
     * @param eventId UUID to identify the Event
     * @param checkInQRCode the check-in QRCode
     * @param promoQRCode the promotional PromoQRCode
     * @param poster the Event's EventPoster
     * @param eventName the Event's name String
     * @param eventTime the Event's time String
     * @param eventLocation the Event's location String
     * @param eventDescription the Event's description String
     */
    public Event(UUID eventId, QRCode checkInQRCode, PromoQRCode promoQRCode, EventPoster poster, String eventName, String eventTime, String eventLocation, String eventDescription) {

        this.eventId = eventId;
        this.checkInQRCode = checkInQRCode;
        this.promoQRCode = promoQRCode;
        this.poster = poster;
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
    }

    /**
     * Returns the Event's unique ID
     * @return eventId the UUID of the Event
     */
    public UUID getEventId(){
        return eventId;
    }

    /**
     * Returns the ArrayList of Attendees who have checked into the Event
     * @return attendees ArrayList of Attendees
     */
    public ArrayList<Attendee> getAttendee() {
        return attendees;
    }

    /**
     * Returns the ArrayList of signups who have signed-up for the Event
     * @return signups ArrayList of Attendees
     */
    public ArrayList<Attendee> getSignups() {
        return signups;
    }


    public QRCode getCheckInQRCode() {
        return checkInQRCode;
    }

    public void setCheckInQRCode(QRCode checkInQRCode) {
        this.checkInQRCode = checkInQRCode;
    }

    public PromoQRCode getPromoQRCode() {
        return promoQRCode;
    }

    public void setPromoQRCode(PromoQRCode promoQRCode) {
        this.promoQRCode = promoQRCode;
    }

    public EventPoster getPoster() {
        return poster;
    }

    public void setPoster(EventPoster poster) {
        this.poster = poster;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
