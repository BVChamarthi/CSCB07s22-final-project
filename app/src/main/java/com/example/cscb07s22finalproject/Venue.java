package com.example.cscb07s22finalproject;

import java.util.ArrayList;

public class Venue {

    String name;
    ArrayList<String> activities;
    ArrayList<Event> events;

    public Venue(String name, ArrayList<String> activities, ArrayList<Event> events)
    {
        this.name = name;
        this.activities = activities;
        this.events = events;
    }
}
