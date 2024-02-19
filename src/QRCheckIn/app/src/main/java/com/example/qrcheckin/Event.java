package com.example.qrcheckin;

import java.util.ArrayList;

public class Event {
    private AttendeeList Attendee;
    private AttendeeList signups;
    private QRCode checkInQRCode;
    private PromoQRCode promoQRCode;
    private EventPoster poster;
    private ArrayList<notification> notifications;

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
