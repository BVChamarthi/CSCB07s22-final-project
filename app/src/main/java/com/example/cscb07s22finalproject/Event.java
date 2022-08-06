package com.example.cscb07s22finalproject;

import java.io.Serializable;

public class Event implements Serializable {
    /*
    Important methods:
        toString(): How info from Event is formatted

    Q: Why Event has venueName field despite there being ArrayList<Event> where I can get venueName from?
        A: Idk how make the scroll view work with a nested ArrayList
     */

    private Venue venue;
    private String name;
    private String activity;
    private String date;
    private String startTime;
    private String endTime;
    private int curParticipants, maxParticipants;

    public Event(String name, Venue venue, String activity, String date,String startTime,String endTime,int curParticipants, int maxParticipants)
    {
        this.name = name;
        this.venue = venue;
        this.activity = activity;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxParticipants = maxParticipants;
        this.curParticipants = curParticipants;
    }

    // Getters and Setters
    public String getEventName()
    {
        return name;
    }

    public Venue getVenue() {
        return venue;
    }
    public void setVenue(Venue v) { venue = v; }

    public String getActivity()
    {
        return activity;
    }

    public String getDate() {
        return date;
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
        return  "\t" + name +
                "\n\t" + venue.name +
                "\n\t" + activity +
                "\n\t" + startTime + " - " + endTime +
                "\n\tPlayers: " + curParticipants +
                "/" + maxParticipants;
    }
}