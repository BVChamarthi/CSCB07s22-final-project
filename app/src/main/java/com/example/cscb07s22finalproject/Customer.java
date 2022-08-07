package com.example.cscb07s22finalproject;

import java.util.ArrayList;

public class Customer extends User
{
    // Note: Renamed bookedEvents to joinedEvents to avoid confusion
    private ArrayList<Integer> joinedEvents;
    private ArrayList<Integer> scheduledEvents;

    public Customer(String username, String password)
    {
        super(username, password);

        // Instantiating lists
        joinedEvents = new ArrayList<Integer>();
        scheduledEvents = new ArrayList<Integer>();
    }

    public void joinEvent(int eventCode)
    {
        joinedEvents.add(eventCode);
    }

    public void addScheduledEventToUser(int eventNum)
    {
        scheduledEvents.add(eventNum);
    }

    // Getters and Setters
    public ArrayList<Integer> getJoinedEvents() {
        return joinedEvents;
    }

    public void setJoinedEvents(ArrayList<Integer> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    public ArrayList<Integer> getScheduledEvents() {
        return scheduledEvents;
    }

    public void setScheduledEvents(ArrayList<Integer> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
    }

    // Getters and Setters
    public ArrayList<Integer> getJoinedEvents() {
        return joinedEvents;
    }

    public void setJoinedEvents(ArrayList<Integer> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    public ArrayList<Integer> getScheduledEvents() {
        return scheduledEvents;
    }

    public void setScheduledEvents(ArrayList<Integer> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
    }
}
