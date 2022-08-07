package com.example.cscb07s22finalproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Venue implements Serializable {

    String venueName;
    ArrayList<String> activities;
    ArrayList<Event> events;


    public Venue(String venueName, ArrayList<String> activities) {
        this.venueName = venueName;
        this.activities = activities;
        this.events = new ArrayList<Event>();
    }

    public int addEvent(Event event)
    {
        //Not sure if .contains() properly compares two objs, might have to change 2nd predicate
        // implemented Event.equals(), hopefully events are compared properly now
        if(activities.contains(event.getActivity()) && !(events.contains(event))){
            event.setParentVenue(this);
            events.add(event);
            return 0;
        } else return 1;
    }

    public void addEventNoCheck(Event e) {
        e.setParentVenue(this);
        events.add(e);
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

    public String getVenueName() {
        return venueName;
    }

    public ArrayList<String> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<String> activities) {
        this.activities = activities;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;

    }

    public ArrayList<Event> getEvents() { return events; }

    @Override
    public String toString() {
        return venueName;
    }

    public String getVenueAsString() {
        String venueToString = "\t" + venueName + "\n\n";

        for(String activity : activities)
        {
            venueToString += "\t" + activity + "\n";
        }

        return venueToString.trim();
    }
}
