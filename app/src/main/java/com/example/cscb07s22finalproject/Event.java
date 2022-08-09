package com.example.cscb07s22finalproject;

import java.io.Serializable;

public class Event implements Serializable {
    private String eventName;
    private String activity;
    private String date;
    private String startTime;
    private String endTime;
    private Venue parentVenue;
    private int curParticipants, maxParticipants;
    private boolean isChecked = false;


    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Event)) return false;   // also checks for null
        Event objEvent = (Event)obj;
        return (
                objEvent.getEventName() == eventName &&
                        objEvent.getActivity() == activity &&
                        objEvent.getDate() == date &&
                        objEvent.getStartTime() == startTime &&
                        objEvent.getEndTime() == endTime &&
                        objEvent.getMaxParticipants() == maxParticipants &&
                        objEvent.getParentVenue() == parentVenue
                );
    }

    public Event(String eventName, Venue parentVenue, String activity, String date,String startTime,String endTime,int curParticipants, int maxParticipants)

    {
        this.eventName = eventName;
        this.parentVenue = parentVenue;
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
        return eventName;
    }
    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }


    public Venue getParentVenue() { return parentVenue; }
    public void setParentVenue(Venue parentVenue) { this.parentVenue = parentVenue; }

    public String getActivity()
    {
        return activity;
    }

    public void setActivity(String activity)
    {
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime()
    {
        return startTime;
    }
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public int getCurParticipants() {
        return curParticipants;
    }
    public void setCurParticipants(int curParticipants) {
        this.curParticipants = curParticipants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void addParticipant() {curParticipants++;}

    @Override
    public String toString() {
        return  "\t" + eventName +
                "\n\t" + parentVenue.getVenueName() +
                "\n\t" + activity +
                "\n\t" + date + ", " + startTime + " - " + endTime +
                "\n\tPlayers: " + curParticipants + "/" + maxParticipants;
    }
}