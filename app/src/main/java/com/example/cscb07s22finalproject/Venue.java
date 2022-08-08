package com.example.cscb07s22finalproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Venue implements Serializable {
    DataBase db = DataBase.getInstance();

    String venueName;
    ArrayList<String> activities;
//    ArrayList<Event> events;
    ArrayList<Integer> events;


    public Venue(String venueName, ArrayList<String> activities) {
        this.venueName = venueName;
        this.activities = activities;
        this.events = new ArrayList<Integer>();
    }

    public Venue(String venueName, ArrayList<String> activities, ArrayList<Integer> events) {
        this.venueName = venueName;
        this.activities = activities;
        this.events = events;
    }

    public int addEvent(int eventCode)
    {
        /*
        //Not sure if .contains() properly compares two objs, might have to change 2nd predicate
        // implemented Event.equals(), hopefully events are compared properly now
        if(activities.contains(event.getActivity()) && !(events.contains(event))){
            event.setParentVenue(this);
            events.add(event);
            return 0;
        } else return 1;

         */
        Event event = db.getEvents().get(eventCode);
        if(activities.contains(event.getActivity()) && !(events.contains(eventCode))) {
            event.setParentVenue(this);
            events.add(eventCode);
            return 0;
        }
        else return 1;
    }

    public void removeEvent(int eventCode){
        events.remove(eventCode);
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

    public void setVenueName(String venueName) { this.venueName = venueName; }

/*
    public void addEventCodeToVenue(int eventCode)
    {
        codes.add(eventCode);
    }

    public ArrayList<Integer> getCodes() {
        return codes;
    }
*/
    public ArrayList<Integer> getEvents() { return events; }

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

