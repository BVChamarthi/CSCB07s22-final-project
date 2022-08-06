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

    // Suggestion: If the user wants to schedule an event, wouldn't the user want to
    //             take in the following parameters:

    //             String venueName, String activities,
    //             int startTime, int endTime, int capacity

    //             Then, we generate an eventCode using how many events are currently in the database

    //             Then, we upload the eventCode to the database, and under that "tree", we
    //             put all the parameters (venue, activities, etc.)
    public void scheduleEvent()
    {

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
