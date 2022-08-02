package com.example.cscb07s22finalproject;

import java.io.Serializable;

public class Event implements Serializable {
    private String eventName;
    private String activity;
    private String startTime;
    private String endTime;
    private int curParticipants, maxParticipants;
    private boolean isChecked = false;

    public Event(String eventName, String activity,String startTime,String endTime,int curParticipants, int maxParticipants)
    {
        this.eventName = eventName;
        this.activity = activity;
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

    public String getActivity()
    {
        return activity;
    }

    public void setActivity(String activity)
    {
        this.activity = activity;
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

    @Override
    public String toString() {
        return  eventName +
                "\n\t" + activity +
                "\n\t" + startTime +
                ": " + endTime +
                "\n\tPlayers: " + curParticipants +
                "/" + maxParticipants;
    }
}