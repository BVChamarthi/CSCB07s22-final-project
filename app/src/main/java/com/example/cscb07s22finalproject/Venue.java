package com.example.cscb07s22finalproject;

import java.util.ArrayList;

public class Venue {

    String name;
    ArrayList<String> activities;
    ArrayList<Event> events;

    public Venue(String venueName, ArrayList<String> activities) {
        this.name = venueName;
        this.activities = activities;
        this.events = new ArrayList<Event>();
    }

    public int addEvent(Event event) {
        //Not sure if .contains() properly compares two objs, might have to change 2nd predicate
        if(activities.contains(event.getActivity()) && !(events.contains(event))){
            event.setVenue(this);
            events.add(event);
            return 0;
        } else return 1;
    }

    public void removeEvent(Event event){
        events.remove(event);
        //Even if event is not in events list still say its removed anyways cause technically it has been removed from list
    }

    public void addAllowedActivities(String activity){
        activities.add(activity);
    }

    public void removeAllowedActivities(String activity){
        activities.remove(activity);
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getActivities() {
        return activities;
    }

    public void setName(String name) {
        this.name = name;
    }
}
