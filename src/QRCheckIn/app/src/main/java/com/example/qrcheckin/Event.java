package com.example.qrcheckin;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {
    private AttendeeList Attendee;
    private AttendeeList signups;
    private QRCode checkInQRCode;
    private PromoQRCode promoQRCode;
    private EventPoster poster;
    private ArrayList<Notification> notifications;
    private String eventName;
    private String eventTime;
    private String eventLocation;
    private String eventDescription;

    public Event(AttendeeList attendee, AttendeeList signups, QRCode checkInQRCode, PromoQRCode promoQRCode, EventPoster poster, ArrayList<Notification> notifications, String eventName, String eventTime, String eventLocation, String eventDescription) {
        Attendee = attendee;
        this.signups = signups;
        this.checkInQRCode = checkInQRCode;
        this.promoQRCode = promoQRCode;
        this.poster = poster;
        this.notifications = notifications;
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
    }

    public AttendeeList getAttendee() {
        return Attendee;
    }

    public void setAttendee(AttendeeList attendee) {
        Attendee = attendee;
    }

    public AttendeeList getSignups() {
        return signups;
    }

    public void setSignups(AttendeeList signups) {
        this.signups = signups;
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
        this.eventDescription = eventDescription;}

    public ArrayList<notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<notification> notifications) {
        this.notifications = notifications;
    }

    public Event(AttendeeList attendee, AttendeeList signups, QRCode checkInQRCode, PromoQRCode promoQRCode, EventPoster poster, ArrayList<notification> notifications) {
        Attendee = attendee;
        this.signups = signups;
        this.checkInQRCode = checkInQRCode;
        this.promoQRCode = promoQRCode;
        this.poster = poster;
        this.notifications = notifications;
    }
}