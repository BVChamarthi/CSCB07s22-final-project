package com.example.cscb07s22finalproject;

import java.io.Serializable;

public class Event implements Serializable {
    /*
    Important methods:
        toString(): How info from Event is formatted

    Q: Why Event has venueName field despite there being ArrayList<Event> where I can get venueName from?
        A: Idk how make the scroll view work with a nested ArrayList
     */

    private String venueName;
    private String eventName;
    private String activity;
    private String date;
    private String startTime;
    private String endTime;
    private Venue parentVenue;
    private int curParticipants, maxParticipants;


/*    public Event(String eventName,
                 String venueName,
                 String activity,
                 String date,
                 String startTime,
                 String endTime,
                 int curParticipants,
                 int maxParticipants)
    {
        this.eventName = eventName;
        this.venueName = venueName;
        this.activity = activity;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.parentVenue = null;
        this.maxParticipants = maxParticipants;
        this.curParticipants = curParticipants;
    }*/

    public Event(String eventName,
                 Venue parentVenue,
                 String activity,
                 String date,
                 String startTime,
                 String endTime,
                 int curParticipants,
                 int  maxParticipants) {
        this.eventName = eventName;
        this.parentVenue = parentVenue;
        this.venueName = parentVenue.getVenueName();
        this.activity = activity;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.curParticipants = curParticipants;
        this.maxParticipants = maxParticipants;
    }

    // Getters and Setters
    public String getEventName()
    {
        return eventName;
    }

    public Venue getVenue() {
        return parentVenue;
    }
    public void setVenue(Venue v) {
        parentVenue = v;
        venueName = v.getVenueName();
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getActivity()
    {
        return activity;
    }

    public String getDate() {
        return date;

    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setActivity(String activity)
    {
        this.activity = activity;

    }

    public String getStartTime()
    {
        return startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public int getCurParticipants() {
        return curParticipants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    @Override
    public String toString() {
        return  "\t" + eventName +
                "\n\t" + venueName +
                "\n\t" + activity +
                "\n\t" + startTime + " - " + endTime +
                "\n\tPlayers: " + curParticipants +
                "/" + maxParticipants;
    }
}