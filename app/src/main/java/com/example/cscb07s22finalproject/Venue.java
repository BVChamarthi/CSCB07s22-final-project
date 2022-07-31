package com.example.cscb07s22finalproject;

import java.util.ArrayList;

public class Venue
{
    private String venueName;
    private ArrayList<String> activities;
    private int capacity;

    public Venue(String venueName, ArrayList<String> activities, int capacity)
    {
        this.venueName = venueName;
        this.activities = activities;
        this.capacity = capacity;
    }

    public void addActivity(String newActivity)
    {
        this.activities.add(newActivity);
    }

    // Getters and Setters
    public String getVenueName()
    {
        return venueName;
    }

    public void setVenueName(String venueName)
    {
        this.venueName = venueName;
    }

    public ArrayList<String> getActivities()
    {
        return activities;
    }

    public void setActivities(ArrayList<String> activities)
    {
        this.activities = activities;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }
}
